package chenps3.dynamicproxy.util;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 通过setAccessible(true)，关闭方法调用时对可访问性的检查，从而提升性能
 * 禁用Booster
 * -Dchenps3.dynamicproxy.util.MethodTurboBooster.disabled=true
 * @Author chenguanhong
 * @Date 2021/8/18
 */
public final class MethodTurboBooster {

    private static final Booster BOOSTER =
            Boolean.getBoolean(MethodTurboBooster.class.getName() + ".disabled") ?
                    new BoosterOff() : new BoosterOn();

    public static <E> E boost(E proxy) {
        return BOOSTER.turboBoost(proxy);
    }

    public static Method boost(Method method) {
        return BOOSTER.turboBoost(method);
    }

    private interface Booster {

        <E> E turboBoost(E proxy);

        Method turboBoost(Method method);
    }

    private static class BoosterOn implements Booster {

        @Override
        public <E> E turboBoost(E proxy) {
            if (!(proxy instanceof Proxy)) {
                throw new IllegalArgumentException("只能优化Proxy实例");
            }
//            System.out.println("booster on " + proxy.getClass().getSimpleName());
            try {
                for (var field : proxy.getClass().getDeclaredFields()) {
                    if (field.getType() == Method.class) {
                        field.setAccessible(true);
                        turboBoost((Method) field.get(null));
                    }
                }
                return proxy;
            } catch (IllegalAccessException | RuntimeException e) {
                return proxy;
            }
        }

        @Override
        public Method turboBoost(Method method) {
            try {
                method.setAccessible(true);
            } catch (Exception e) {
                //可能会报错，此时忽略，返回原方法即可
            }
//            System.out.println("booster on " + method.getName());
            return method;
        }
    }

    private static class BoosterOff implements Booster {

        @Override
        public <E> E turboBoost(E proxy) {
            if (!(proxy instanceof Proxy)) {
                throw new IllegalArgumentException("只能优化Proxy实例");
            }
//            System.out.println("booster off " + proxy.getClass().getSimpleName());
            return proxy;
        }

        @Override
        public Method turboBoost(Method method) {
//            System.out.println("booster off " + method.getName());
            return method;
        }
    }
}
