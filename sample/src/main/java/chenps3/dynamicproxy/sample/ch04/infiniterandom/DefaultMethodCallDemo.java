package chenps3.dynamicproxy.sample.ch04.infiniterandom;

import chenps3.dynamicproxy.Proxies;

import java.util.Iterator;

/**
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public class DefaultMethodCallDemo {

    public static void main(String[] args) {
        Iterator<Double> filter = Proxies.filter(Iterator.class, new InfiniteRandomDouble());
        System.out.println(filter.hasNext());
        System.out.println(filter.next());

        try {
            filter.remove();
            throw new AssertionError("期望获取到一个UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
        }

        System.out.println("无限流...");
        filter.forEachRemaining(i -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
            }
            System.out.println(i);
        });
    }
}
