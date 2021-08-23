package chenps3.dynamicproxy.protect;

import chenps3.dynamicproxy.sample.ch02.virtual.CustomHashMap;
import org.junit.Test;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public class CustomMapTest extends ConcurrentTest {

    @Test
    public void test() {
        check(new CustomHashMap<>());
    }
}
