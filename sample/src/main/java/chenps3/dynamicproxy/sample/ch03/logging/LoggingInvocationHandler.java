package chenps3.dynamicproxy.sample.ch03.logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author chenguanhong
 * @Date 2021/8/18
 */
public class LoggingInvocationHandler implements InvocationHandler {

    private final Logger logger;

    private final Object obj;

    public LoggingInvocationHandler(Logger logger, Object obj) {
        this.logger = logger;
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info(() -> "开始: " + toString(method, args));
        final boolean logFine = logger.isLoggable(Level.FINE);
        long start = logFine ? System.nanoTime() : 0;
        try {
            return method.invoke(obj, args);
        } finally {
            long nanos = logFine ? System.nanoTime() - start : 0;
            logger.info(() -> "结束: " + toString(method, args));
            if (logFine) {
                logger.info(() -> "耗时: " + nanos + "ns");
            }
        }
    }

    private String toString(Method method, Object[] args) {
        return String.format("%s.%s(%s)",
                method.getDeclaringClass().getCanonicalName(),
                method.getName(),
                args == null ? "" :
                        Stream.of(args).map(String::valueOf)
                                .collect(Collectors.joining(", ")));
    }
}
