package chenps3.dynamicproxy.sample.ch03.gotchas;

import chenps3.dynamicproxy.Proxies;

import java.io.IOException;

/**
 * @Author chenguanhong
 * @Date 2021/8/20
 */
public class UndeclaredExceptionThrown {

    public static void main(String[] args) {
        Runnable job = Proxies.castProxy(Runnable.class,((proxy, method, args1) -> {
            throw new IOException("bad exception");
        }));
        job.run();
    }
}
