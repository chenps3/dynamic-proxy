package chenps3.dynamicproxy.sample.handler;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 对subject的所有方法调用加锁
 * @Author chenguanhong
 * @Date 2021/8/19
 */
public class SynchronizedHandler<S> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 1L;

    private final S subject;

    public SynchronizedHandler(S subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        synchronized (proxy) {
            return method.invoke(subject, args);
        }
    }
}
