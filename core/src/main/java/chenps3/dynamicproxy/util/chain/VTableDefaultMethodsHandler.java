package chenps3.dynamicproxy.util.chain;

import chenps3.dynamicproxy.util.VTable;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 接口默认方法的调用，不需要receiver对象，而是在代理参数上调用
 *
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public class VTableDefaultMethodsHandler extends ChainedInvocationHandler {

    private final VTable vtable;

    public VTableDefaultMethodsHandler(VTable vtable, ChainedInvocationHandler next) {
        super(next);
        this.vtable = Objects.requireNonNull(vtable);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodHandle match = vtable.lookupDefaultMethod(method);
        if (match != null) {
            return match.invoke(proxy, args);
        }
        return super.invoke(proxy, method, args);
    }

    @Override
    protected Stream<Method> findUnhandledMethods(Class<?>... targets) {
        return super.findUnhandledMethods(targets).filter(method -> vtable.lookupDefaultMethod(method) == null);
    }
}
