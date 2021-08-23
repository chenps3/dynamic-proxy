package chenps3.dynamicproxy.protect;

import chenps3.dynamicproxy.Proxies;
import chenps3.dynamicproxy.sample.ch02.virtual.CustomHashMap;
import chenps3.dynamicproxy.sample.ch02.virtual.ICustomMap;
import org.junit.Test;

/**
 * @Author chenguanhong
 * @Date 2021/8/19
 */
public class SynchronizedTest extends ConcurrentTest{

    @Test
    public void test() {
        check(Proxies.synchronizedProxy(ICustomMap.class,new CustomHashMap<>()));
    }
}
