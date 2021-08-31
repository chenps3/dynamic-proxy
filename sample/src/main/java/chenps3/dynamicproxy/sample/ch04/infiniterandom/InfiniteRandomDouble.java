package chenps3.dynamicproxy.sample.ch04.infiniterandom;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成一个Double实例的无限流
 * 这个类并没有实现iterator接口，但通过动态代理的decorator，可以创建一个类似的迭代器
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public class InfiniteRandomDouble {

    public boolean hasNext() {
        return true;
    }

    public Double next() {
        return ThreadLocalRandom.current().nextDouble();
    }
}
