package chenps3.dynamicproxy.util.chain;

import chenps3.dynamicproxy.util.VTable;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 在receiver上进行方法调用
 * vtable用来匹配目标接口和匹配方法
 * next连接责任链
 *
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public class VTableHandler extends ChainedInvocationHandler {

    private final VTable vtable;
    private final Object receiver;

    public VTableHandler(Object receiver, VTable vtable, ChainedInvocationHandler next) {
        super(next);
        this.vtable = Objects.requireNonNull(vtable);
        this.receiver = Objects.requireNonNull(receiver);
    }

    /**
     * 查找method，如果找到了，在receiver上进行调用；否则传到责任链中
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method match = vtable.lookup(method);
        if (match != null) {
            return match.invoke(receiver, args);
        }
        return super.invoke(proxy, method, args);
    }

    /**
     * 从父类返回的Stream中移除在vtable中可找到的方法
     */
    @Override
    protected Stream<Method> findUnhandledMethods(Class<?>... targets) {
        return super.findUnhandledMethods(targets).filter(method -> vtable.lookup(method) == null);
    }
}
