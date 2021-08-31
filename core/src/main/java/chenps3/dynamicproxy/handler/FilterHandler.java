package chenps3.dynamicproxy.handler;

import chenps3.dynamicproxy.util.VTable;
import chenps3.dynamicproxy.util.VTables;
import chenps3.dynamicproxy.util.chain.ChainedInvocationHandler;
import chenps3.dynamicproxy.util.chain.VTableDefaultMethodsHandler;
import chenps3.dynamicproxy.util.chain.VTableHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public class FilterHandler implements InvocationHandler {

    private final ChainedInvocationHandler chain;

    public FilterHandler(Class<?> filter, Object component) {
        VTable vt = VTables.newVTable(component.getClass(), filter);
        VTable defaultVT = VTables.newDefaultMethodVTable(filter);
        chain = new VTableHandler(component, vt, new VTableDefaultMethodsHandler(defaultVT, null));
        chain.checkAllMethodsAreHandled(filter);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return chain.invoke(proxy, method, args);
    }
}
