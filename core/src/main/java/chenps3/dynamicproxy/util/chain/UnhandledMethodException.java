package chenps3.dynamicproxy.util.chain;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public class UnhandledMethodException extends IllegalArgumentException {

    private final Collection<Method> unhandled;

    /**
     * 限定在chain包内使用，没啥特殊意义
     */
    UnhandledMethodException(Collection<Method> unhandled) {
        super("Unhandled methods: " + unhandled);
        this.unhandled = List.copyOf(unhandled);
    }

    public Collection<Method> getUnhandled() {
        return unhandled;
    }
}
