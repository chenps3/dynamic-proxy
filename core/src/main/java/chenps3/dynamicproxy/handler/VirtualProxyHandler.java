package chenps3.dynamicproxy.handler;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 延迟初始化
 *
 * @Author chenguanhong
 * @Date 2021/8/19
 */
public class VirtualProxyHandler<S> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 1L;

    private final Supplier<? extends S> subjectSupplier;

    private S subject;

    public VirtualProxyHandler(Supplier<? extends S> subjectSupplier) {
        this.subjectSupplier = subjectSupplier;
    }

    private S getSubject() {
        if (subject == null) {
            subject = subjectSupplier.get();
            System.out.println("创建对象" + subject.getClass().getSimpleName());
        }
        return subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(getSubject(), args);
    }
}
