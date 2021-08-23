package chenps3.dynamicproxy.test.handcraft.protect;

import chenps3.dynamicproxy.ch02.protect.SynchronizedCustomMap;
import chenps3.dynamicproxy.ch02.virtual.CustomHashMap;
import org.junit.Test;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public class SynchronizedCustomMapTest extends CustomMapTest {

    @Test
    public void test() {
        check(new SynchronizedCustomMap<>(new CustomHashMap<>()));
    }
}
