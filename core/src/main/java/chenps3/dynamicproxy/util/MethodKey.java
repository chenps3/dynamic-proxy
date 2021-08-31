package chenps3.dynamicproxy.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 按方法名称和参数类型比较Method，
 * 实现了equals hashcode toString compareTo
 * 可以作为map的key
 * MethodKey不知道Method的返回类型
 *
 * @Author chenguanhong
 * @Date 2021/8/26
 */
public class MethodKey implements Comparable<MethodKey> {

    private final String name;

    private final Class<?>[] paramTypes;

    public MethodKey(Method method) {
        this.name = method.getName();
        this.paramTypes = ParameterTypeFetcher.get(method);
    }

    public MethodKey(Class<?> clz, String methodName, Class<?>... paramTypes) {
        try {
            //校验方法名是否存在
            Method method = clz.getMethod(methodName, paramTypes);
            //方法名都是interned（在常量池里）
            this.name = method.getName();
            //确保参数非空
            this.paramTypes = Objects.requireNonNull(paramTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public int compareTo(MethodKey o) {
        //先比较方法名字
        int result = this.name.compareTo(o.name);
        if (result != 0) {
            return result;
        }
        //同名时比较参数类型名称
        return Arrays.compare(this.paramTypes, o.paramTypes, Comparator.comparing(Class::getName));
    }

    @Override
    public String toString() {
        return Stream.of(paramTypes)
                .map(Class::getName)
                .collect(Collectors.joining(", ", name + "(", ")"));
    }

    @Override
    public int hashCode() {
        return name.hashCode() + paramTypes.length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MethodKey)) {
            return false;
        }
        MethodKey that = (MethodKey) obj;
        //可以用==比较，因为方法名都在常量池里
        return that.name == this.name && equalParamTypes(this.paramTypes, that.paramTypes);
    }

    private boolean equalParamTypes(Class<?>[] p1, Class<?>[] p2) {
        if (p1.length != p2.length) {
            return false;
        }
        for (int i = 0; i < p1.length; i++) {
            if (p1[i] != p2[i]) {
                return false;
            }
        }
        return true;
    }
}
