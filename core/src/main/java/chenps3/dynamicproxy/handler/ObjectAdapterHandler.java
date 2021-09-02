package chenps3.dynamicproxy.handler;

import chenps3.dynamicproxy.util.VTable;
import chenps3.dynamicproxy.util.VTables;
import chenps3.dynamicproxy.util.chain.ChainedInvocationHandler;
import chenps3.dynamicproxy.util.chain.VTableDefaultMethodsHandler;
import chenps3.dynamicproxy.util.chain.VTableHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author chenguanhong
 * @Date 2021/9/2
 */
public final class ObjectAdapterHandler implements InvocationHandler {

    private final ChainedInvocationHandler chain;

    /**
     * @param target  期望适配的接口，比如BetterCollection
     * @param adaptee 对象适配器被适配的对象
     * @param adapter 该对象包含了用来实现target接口
     */
    public ObjectAdapterHandler(Class<?> target, Object adaptee, Object adapter) {
        VTable adapterVT = VTables.newVTable(adapter.getClass(), target);
        VTable adapteeVT = VTables.newVTable(adaptee.getClass(), target);
        VTable defaultVT = VTables.newDefaultMethodVTable(target);
        chain = new VTableHandler(adapter, adapterVT, new VTableHandler(adaptee, adapteeVT, new VTableDefaultMethodsHandler(defaultVT, null)));
        chain.checkAllMethodsAreHandled(target);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return chain.invoke(proxy, method, args);
    }
}
