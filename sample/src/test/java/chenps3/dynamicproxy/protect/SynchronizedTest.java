package chenps3.dynamicproxy.test.handcraft.protect;

import chenps3.dynamicproxy.Proxies;
import chenps3.dynamicproxy.ch02.virtual.CustomHashMap;
import chenps3.dynamicproxy.ch02.virtual.ICustomMap;
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
