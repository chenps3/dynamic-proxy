package chenps3.dynamicproxy.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 开启方式
 * -Dchenps3.dynamicproxy.sample.util.ParameterTypeFetcher.enabled=true
 *
 * @Author chenguanhong
 * @Date 2021/8/26
 */
public final class ParameterTypeFetcher {

    //    Boolean.getBoolean(name),读取系统变量
    private static final Function<Method, Class<?>[]> PARAMETER_TYPE_FETCHER =
            Boolean.getBoolean(ParameterTypeFetcher.class.getName() + ".enabled") ? new NormalParameterFetcher() : new FastParameterFetcher();

    public static Class<?>[] get(Method method) {
        return PARAMETER_TYPE_FETCHER.apply(method);
    }

    private static class NormalParameterFetcher implements Function<Method, Class<?>[]> {
        @Override
        public Class<?>[] apply(Method method) {
            //method.getParameterTypes 方法会对数组进行clone操作，性能相对较差
            return method.getParameterTypes();
        }
    }

    private static class FastParameterFetcher implements Function<Method, Class<?>[]> {

        private static final VarHandle METHOD_PARAMETER_TYPES;

        static {
            try {
                METHOD_PARAMETER_TYPES = MethodHandles.privateLookupIn(Method.class, MethodHandles.lookup())
                        .findVarHandle(Method.class, "parameterTypes", Class[].class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new Error(e);
            }
        }

        @Override
        public Class<?>[] apply(Method method) {
            return (Class<?>[]) METHOD_PARAMETER_TYPES.get(method);
        }
    }
}
