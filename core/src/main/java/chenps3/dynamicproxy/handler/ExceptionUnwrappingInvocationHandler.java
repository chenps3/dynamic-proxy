package chenps3.dynamicproxy.handler;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 抛出下层调用的异常
 * @Author chenguanhong
 * @Date 2021/8/18
 */
public class ExceptionUnwrappingInvocationHandler implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 1L;

    private final InvocationHandler handler;

    public ExceptionUnwrappingInvocationHandler(InvocationHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return handler.invoke(proxy, method, args);
        } catch (InvocationTargetException exception) {
            throw exception.getCause();
        }
    }

    public InvocationHandler getNestedInvocationHandler() {
        return handler;
    }
}
