package chenps3.dynamicproxy.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 建立接口方法和实现类方法的映射
 * 为减少对象创建，不使用hashmap，使用若干个等长的数组保存
 * hashcode取方法名的hashcode+参数数量
 * distinctName保存名称唯一的方法，如果名称唯一，就不检查参数是否匹配了
 * 关键方法：
 * lookup
 * lookupDefaultMethod
 *
 * @Author chenguanhong
 * @Date 2021/8/26
 */
public final class VTable {
    //用于实现hash表
    private final Method[] entries;
    //与entries里的Method对应
    private final Class<?>[][] paramTypes;
    //与entries里的Method对应
    private final boolean[] distinctName;
    //与entries里的Method对应
    private final MethodHandle[] defaultMethods;
    private final int size;
    //用于实现hash表
    private final int mask;

    /**
     * 构造函数，在builder中使用
     *
     * @param methods               VTableb包含的方法
     * @param distinctMethodNames   名字没被重载的方法名
     * @param includeDefaultMethods 是否包含默认方法
     */
    private VTable(Collection<Method> methods, Set<String> distinctMethodNames, boolean includeDefaultMethods) {
        this.size = methods.size();
        //numberOfLeadingZeros返回int入参（32位）的二进制串从左边算起连续的0的数量
        //11111...111做无符号右移，如果结果<127，则取127。
        //即mask的值总是2^n-1且最小为127。简单起见我们用开放定址法+线性探测来处理散列冲突，所以数组设置大一些
        mask = Math.max((-1 >>> Integer.numberOfLeadingZeros(size * 4 - 1)), 127);
        entries = new Method[mask + 1];
        paramTypes = new Class<?>[entries.length][];
        distinctName = new boolean[entries.length];
        defaultMethods = new MethodHandle[entries.length];
        for (var method : methods) {
            put(method, distinctMethodNames.contains(method.getName()), includeDefaultMethods);
        }
        methods.forEach(MethodTurboBooster::boost);
    }


    /**
     * 查询接口方法对应的实现类方法，如果找不到返回null
     */
    public Method lookup(Method method) {
        int index = findIndex(method);
        return index < 0 ? null : entries[index];
    }

    /**
     * 查询接口默认方法，如果找不到返回null
     */
    public MethodHandle lookupDefaultMethod(Method method) {
        int index = findIndex(method);
        return index < 0 ? null : defaultMethods[index];
    }

    /**
     * VTable的大小，构造完成后就不会变了
     */
    public int size() {
        return size;
    }

    /**
     * VTable里Method的Stream，用于ChainedInvocationHandler验证目标对象的所有方法是否都被VTable覆盖
     */
    public Stream<Method> stream() {
        return Stream.of(entries).filter(Objects::nonNull);
    }

    /**
     * VTable里接口默认方法的Stream，用于ChainedInvocationHandler验证目标对象的所有方法是否都被VTable覆盖
     */
    public Stream<Method> streamDefaultMethods() {
        return IntStream.iterate(0, i -> i < entries.length, i -> i + 1)
                .filter(i -> defaultMethods[i] != null)
                .mapToObj(i -> entries[i]);
    }

    /**
     * 找一个空闲的数组位置插入元素，方法如果重复了会抛出异常
     */
    private void put(Method method, boolean distinct, boolean includeDefaultMethods) {
        int index = findIndex(method);
        if (index >= 0) {
            throw new IllegalArgumentException("方法重复了:" + new MethodKey(method));
        }
        index = ~index;//取反得到一个可用的下标
        entries[index] = method;
        paramTypes[index] = ParameterTypeFetcher.get(method);
        distinctName[index] = distinct;
        if (includeDefaultMethods && method.isDefault()) {
            defaultMethods[index] = getDefaultMethodHandle(method);
        }
    }

    /**
     * 如果默认接口方法的module可用，返回默认接口方法的MethodHandle
     * 否则返回null
     */
    private MethodHandle getDefaultMethodHandle(Method method) {
        try {
            Class<?> target = method.getDeclaringClass();
            if (isTargetClassInOpenModule(target)) {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                return MethodHandles.privateLookupIn(target, lookup)
                        .unreflectSpecial(method, target)
                        //asSpreader()可以避免调用invokeWithArguments(),快了10倍
                        .asSpreader(Object[].class, method.getParameterCount());
            }
            return null;
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 例如，当前VTable位于模块chenps3.dynamicproxy，我们希望使用java.util接口的默认方法
     * 我们需要显式地开启模块 --add-opens java.base/java.util=chenps3.dynamicproxy
     */
    private boolean isTargetClassInOpenModule(Class<?> target) {
        Module targetModule = target.getModule();
        String targetPackage = target.getPackageName();
        Module outModule = VTable.class.getModule();
        return targetModule.isOpen(targetPackage, outModule);
    }

    /**
     * 返回method的下标；返回负值表示没找到
     * 如果方法名匹配且方法名没有被重载，直接返回下标
     */
    private int findIndex(Method method) {
        int offset = offset(method);
        Class<?>[] methodParamTypes = null;
        Method match;
        while ((match = entries[offset]) != null) {
            //方法名称都在字符串池里，可以直接==比较
            if (match.getName() == method.getName()) {
                if (distinctName[offset]) {
                    return offset;
                }
                if (methodParamTypes == null) {
                    methodParamTypes = ParameterTypeFetcher.get(method);
                }
                if (matches(paramTypes[offset], methodParamTypes)) {
                    return offset;
                }
            }
            //如果没找到下标说明散列冲突，看下一个
            offset = (offset + 1) & mask;
        }
        //找不到方法，返回负数
        return ~offset;
        //对它再次取反，就可以知道数组下一个可用的下标是哪个
    }

    /**
     * 基于方法的名称和参数数量，返回方法初始偏移量
     * 对2^n-1按位与，相当于对2^n取模
     */
    private int offset(Method method) {
        return (method.getName().hashCode() + method.getParameterCount()) & mask;
    }

    /**
     * parameter type快速比较。
     * 可以直接使用==来代替equals
     */
    private boolean matches(Class<?>[] type1, Class<?>[] type2) {
        if (type1.length != type2.length) {
            return false;
        }
        for (int i = 0; i < type1.length; i++) {
            if (type1[i] != type2[i]) {
                return false;
            }
        }
        return true;
    }

    public static class Builder {

        /**
         * 返回更具体的那个类型
         */
        private static final BinaryOperator<Method> MOST_SPECIFIC =
                (m1, m2) -> {
                    var r1 = m1.getReturnType();
                    var r2 = m2.getReturnType();
                    if (r2.isAssignableFrom(r1)) {
                        return m1;
                    }
                    if (r1.isAssignableFrom(r2)) {
                        return m2;
                    }
                    throw new IllegalStateException(m1 + " and " + m2 + "返回类型不兼容");
                };

        //Object基类的方法
        private static final Method[] OBJECT_METHODS;

        static {
            try {
                OBJECT_METHODS = new Method[]{
                        Object.class.getMethod("toString"),
                        Object.class.getMethod("hashCode"),
                        Object.class.getMethod("equals", Object.class),
                };
            } catch (NoSuchMethodException e) {
                throw new Error(e);
            }
        }

        //通过这个map查找装饰后的method
        private final Map<MethodKey, Method> receiverClassMap;
        private List<Class<?>> targetInterfaces = new ArrayList<>();
        private boolean includeObjectMethods = true;
        private boolean includeDefaultMethods = false;
        private boolean ignoreReturnTypes = false;

        /**
         * receiver指接收实际方法调用的class，不一定直接关联动态代理接口
         */
        public Builder(Class<?> receiver) {
            receiverClassMap = createPublicMethodMap(receiver);
        }

        /**
         * VTable里不包含Object的基本方法，默认true
         */
        public Builder excludeObjectMethods() {
            this.includeObjectMethods = false;
            return this;
        }

        /**
         * VTable包含接口默认实现，默认false
         */
        public Builder includeDefaultMethods() {
            this.includeDefaultMethods = true;
            return this;
        }

        /**
         * 忽略返回类型，默认false
         */
        public Builder ignoreReturnTypes() {
            this.ignoreReturnTypes = true;
            return this;
        }

        /**
         * 添加希望映射的接口
         */
        public Builder addTargetInterface(Class<?> targetIntf) {
            if (!targetIntf.isInterface()) {
                throw new IllegalArgumentException(targetIntf.getCanonicalName() + "不是一个接口");
            }
            this.targetInterfaces.add(targetIntf);
            return this;
        }

        public VTable build() {
            //构建目标接口的方法集合，如果要包含Object基类的3个方法，需要手动添加下
            Collection<Method> allMethods = targetInterfaces.stream()
                    .flatMap(clazz -> Stream.of(clazz.getMethods()))
                    .collect(Collectors.toList());
            if (includeObjectMethods) {
                //一共就3个元素，逐个添加即可
                for (Method method : OBJECT_METHODS) {
                    allMethods.add(method);
                }
            }
            //目标接口方法集合如果存在相同名称和参数类型的方法，按返回类型的具体程度合并
            Map<MethodKey, Method> targetMethods = allMethods.stream()
                    .collect(Collectors.toUnmodifiableMap(MethodKey::new, x -> x, MOST_SPECIFIC));
            //找出名字唯一的方法，VTable在进行匹配时，如果名字唯一，就不校验参数了，这样可以有更好的性能
            Set<String> distinctMethodNames = targetMethods.values().stream()
                    .collect(Collectors.groupingBy(Method::getName, Collectors.counting()))
                    .entrySet()
                    .stream().filter(e -> e.getValue() == 1L)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            // 1）方法的methodKey必须在receiverClassMap里
            // 2）确保receiverClassMap方法的返回类型可以转为目标方法的返回类型
            Collection<Method> matchedMethods = targetMethods.entrySet().stream()
                    .map(this::filterOnReturnType)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return new VTable(matchedMethods, distinctMethodNames, includeDefaultMethods);
        }

        /**
         * 确保receiverClassMap方法的返回类型可以转为目标方法的返回类型
         */
        private Method filterOnReturnType(Map.Entry<MethodKey, Method> entry) {
            var receiverMethod = receiverClassMap.get(entry.getKey());
            if (receiverMethod != null) {
                if (ignoreReturnTypes) {
                    return receiverMethod;
                }
                var targetReturn = entry.getValue().getReturnType();
                var receiverReturn = receiverMethod.getReturnType();
                if (targetReturn.isAssignableFrom(receiverReturn)) {
                    return receiverMethod;
                }
            }
            return null;
        }

        private Map<MethodKey, Method> createPublicMethodMap(Class<?> clazz) {
            Map<MethodKey, Method> map = new HashMap<>();
            addTrulyPublicMethods(clazz, map);
            return map;
        }

        /**
         * 递归地把clazz自身、接口、父类的真public方法加入到map
         */
        private void addTrulyPublicMethods(Class<?> clazz, Map<MethodKey, Method> map) {
            //递归的终点
            if (clazz == null) {
                return;
            }
            for (var method : clazz.getMethods()) {
                if (isTrulyPublic(method)) {
                    MethodKey key = new MethodKey(method);
                    //key有可能重复，取返回类型更具体的那个方法
                    map.merge(key, method, MOST_SPECIFIC);
                }
            }
            //递归处理接口
            for (var anInterface : clazz.getInterfaces()) {
                addTrulyPublicMethods(anInterface, map);
            }
            //递归处理父类
            addTrulyPublicMethods(clazz.getSuperclass(), map);
        }

        /**
         * 真public方法：方法时public的，且所属class也是public的
         * 利用java反射的修饰符相关api，通过按位与操作只需要调用一次isPublic
         */
        private boolean isTrulyPublic(Method method) {
            return Modifier.isPublic(method.getModifiers() & method.getDeclaringClass().getModifiers());
        }
    }
}
