package chenps3.dynamicproxy.sample.ch03;

import java.lang.reflect.Proxy;

/**
 * @Author chenguanhong
 * @Date 2021/8/18
 */
public class ProxyName {

    public static void main(String[] args) {
        Object o = Proxy.newProxyInstance(ISODateParser.class.getClassLoader(), new Class<?>[]{ISODateParser.class},
                (proxy, method, arguments) -> null);
        System.out.println(o.getClass().getName());
    }
}
