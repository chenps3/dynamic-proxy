package chenps3.dynamicproxy.ch03.gotchas;

import java.lang.reflect.Proxy;

/**
 * @Author chenguanhong
 * @Date 2021/8/18
 */
public class RecastingExceptionsBroken {

    public static void main(String[] args) {
        @SuppressWarnings("unchecked")
        Comparable<String> comp = (Comparable<String>) Proxy.newProxyInstance(
                Comparable.class.getClassLoader(),
                new Class<?>[]{Comparable.class},
                ((proxy, method, args1) -> method.invoke(args1[0], proxy))
        );
        System.out.println(comp.compareTo("hello"));
    }
}
