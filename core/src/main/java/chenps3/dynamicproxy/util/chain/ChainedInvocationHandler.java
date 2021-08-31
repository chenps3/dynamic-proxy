package chenps3.dynamicproxy.util.chain;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * InvocationHandler的责任链模式
 * 默认调用责任链的下一个元素，如果调用失败，抛出AssertionError
 * 责任链创建后，应该检查目标接口的所有方法是否可以处理调用，通过checkAllMethodAreHandled
 *
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public abstract class ChainedInvocationHandler implements InvocationHandler {

    private final ChainedInvocationHandler next;

    public ChainedInvocationHandler(ChainedInvocationHandler next) {
        this.next = next;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (next != null) {
            return next.invoke(proxy, method, args);
        }
        throw new AssertionError("No InvocationHandler for " + method);
    }

    /**
     * 构建完成后，在责任链的第一个元素调用
     */
    public void checkAllMethodsAreHandled(Class<?>... targets) {
        if (Stream.of(targets).anyMatch(Predicate.not(Class::isInterface))) {
            throw new IllegalArgumentException("入参必须都是接口");
        }
        Collection<Method> unhandled = findUnhandledMethods(targets).collect(Collectors.toList());
        if (!unhandled.isEmpty()) {
            throw new UnhandledMethodException(unhandled);
        }
    }

    /**
     * 责任链的最后一个handler返回一个Stream，包含了target接口的所有method
     * 子类需要调用super.findUnhandledMethods,并添加filter以移除他们处理的方法
     */
    protected Stream<Method> findUnhandledMethods(Class<?>... targets) {
        if (next != null) {
            return next.findUnhandledMethods(targets);
        }
        return Stream.of(targets).map(Class::getMethods)
                .flatMap(Stream::of)
                .filter(m -> !Modifier.isStatic(m.getModifiers()));
    }
}
