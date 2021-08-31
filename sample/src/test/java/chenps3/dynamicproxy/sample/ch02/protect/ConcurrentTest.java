package chenps3.dynamicproxy.sample.ch02.protect;

import chenps3.dynamicproxy.sample.ch02.virtual.ICustomMap;
import org.junit.Assert;

import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * 并行向map插入46000条数据，检查有多少个entry
 *
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public class ConcurrentTest {
    public static final int SQUARES = 46000;

    public void check(ICustomMap<Integer, Integer> map) {
        System.out.println("测试 " + map.getClass().getSimpleName());
        try {
            IntStream.range(0, SQUARES)
                    .parallel()
                    .forEach(i -> map.put(i, i * i));
        } catch (Exception e) {
            e.printStackTrace();
        }
        var entries = new LongAdder();
        map.forEach((k, v) -> entries.increment());
        System.out.println("entries = " + entries);
        System.out.println("map.size() = " + map.size());
        Assert.assertTrue("entries = " + entries + ", map.size()=" + map.size(), entries.intValue() == map.size());
    }
}
