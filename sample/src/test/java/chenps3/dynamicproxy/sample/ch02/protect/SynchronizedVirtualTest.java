package chenps3.dynamicproxy.sample.ch02.protect;

import chenps3.dynamicproxy.Proxies;
import chenps3.dynamicproxy.sample.ch02.virtual.CustomHashMap;
import chenps3.dynamicproxy.sample.ch02.virtual.ICustomMap;
import org.junit.Test;

/**
 * @Author chenguanhong
 * @Date 2021/8/19
 */
public class SynchronizedVirtualTest extends ConcurrentTest {

    @Test
    public void test() {
        ICustomMap<Integer, Integer> map = Proxies.synchronizedProxy(ICustomMap.class,
                 Proxies.<ICustomMap<Integer, Integer>>virtualProxy(ICustomMap.class, CustomHashMap::new));
        check(map);
        System.out.println(map);
    }
}
