package chenps3.dynamicproxy.ch03.logging;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Author chenguanhong
 * @Date 2021/8/18
 */
public class LoggingProxyDemo {

    public static void main(String[] args) {
        Logger log = Logger.getGlobal();
        for (Handler handler : log.getParent().getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(Level.FINE);
            }
        }
        log.setLevel(Level.FINE);

        var handler = new LoggingInvocationHandler(log, new ConcurrentHashMap<>());
        @SuppressWarnings("unchecked")
        var map = (Map<String, Integer>) Proxy.newProxyInstance(Map.class.getClassLoader(),
                new Class<?>[]{Map.class},
                handler);
        map.put("one", 1);
        map.put("two", 2);
        System.out.println(map);
        map.clear();
    }
}
