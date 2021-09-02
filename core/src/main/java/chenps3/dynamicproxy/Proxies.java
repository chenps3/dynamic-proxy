package chenps3.dynamicproxy;

import chenps3.dynamicproxy.handler.*;
import chenps3.dynamicproxy.util.MethodTurboBooster;

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

    /**
     * component过滤掉filter不支持的方法
     */
    public static <F> F filter(Class<? super F> filter, Object component) {
        Objects.requireNonNull(component, "component is null");
        return castProxy(filter, new FilterHandler(filter, component));
    }

    /**
     * 适配target接口
     */
    public static <T> T adapt(Class<? super T> target, Object adaptee, Object adapter) {
        Objects.requireNonNull(adaptee, "adaptee==null");
        Objects.requireNonNull(adaptee, "adapter==null");
        return castProxy(target, new ObjectAdapterHandler(target, adaptee, adapter));
    }
}
