package chenps3.dynamicproxy.sample.ch03.gotchas;

import chenps3.dynamicproxy.handler.ExceptionUnwrappingInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @Author chenguanhong
 * @Date 2021/8/18
 */
public class RecastingExceptionsFixed {

    public static void main(String[] args) {
        @SuppressWarnings("unchecked")
        Comparable<String> comp = (Comparable<String>) Proxy.newProxyInstance(
                Comparable.class.getClassLoader(),
                new Class<?>[]{Comparable.class},
                new ExceptionUnwrappingInvocationHandler((proxy, method, args1) ->
                        //这里相当于调用 入参.compareTo(代理对象)
                        //如果用method.invoke(proxy, args1[0])) 代理对象.compareTo(入参) ，导致无限递归
                        method.invoke(args1[0], proxy))
        );
        System.out.println(comp.compareTo("hello"));
    }
}
