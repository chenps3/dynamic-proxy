package chenps3.dynamicproxy.sample;

import chenps3.dynamicproxy.sample.handler.ExceptionUnwrappingInvocationHandler;
import chenps3.dynamicproxy.sample.handler.SynchronizedHandler;
import chenps3.dynamicproxy.sample.handler.VirtualProxyHandler;
import chenps3.dynamicproxy.sample.util.MethodTurboBooster;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Proxy工厂类，
 *
 * @Author chenguanhong
 * @Date 2021/8/18
 */
public class Proxies {

    @SuppressWarnings("unchecked")
    public static <S> S castProxy(Class<? super S> intf, InvocationHandler handler) {
        Objects.requireNonNull(intf, "intf == null");
        Objects.requireNonNull(handler, "handler == null");
        return MethodTurboBooster.boost(
                (S) Proxy.newProxyInstance(
                        intf.getClassLoader(),
                        new Class<?>[]{intf},
                        new ExceptionUnwrappingInvocationHandler(handler)));
    }

    /**
     * 代理subject的所有方法
     */
    public static <S> S simpleProxy(Class<? super S> subjectInterface, S subject) {
        return castProxy(subjectInterface, ((proxy, method, args) -> method.invoke(subject, args)));
    }

    /**
     * 延迟初始化subject代理
     */
    public static <S> S virtualProxy(Class<? super S> superInterface, Supplier<? extends S> subjectSupplier) {
        Objects.requireNonNull(subjectSupplier, "subjectSupplier==null");
        return castProxy(superInterface, new VirtualProxyHandler<>(subjectSupplier));
    }

    /**
     * 对subject的所有方法加锁
     */
    public static <S> S synchronizedProxy(Class<? super S> subjectInterface, S subject) {
        Objects.requireNonNull(subject, "subject==null");
        return castProxy(subjectInterface, new SynchronizedHandler<>(subject));
    }
}