package chenps3.dynamicproxy.sample.ch05;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 手写class适配器2
 * pros:代码量少；
 * cons:每个类都要单独继承；构造函数依赖继承的类，如果不对外公开就不可行
 * @Author chenguanhong
 * @Date 2021/9/2
 */
public class BetterConcurrentSkipListSet<E> extends ConcurrentSkipListSet<E> implements BetterCollection<E> {

    private final E[] seedArray;

    public BetterConcurrentSkipListSet(E[] seedArray) {
        if (seedArray.length != 0) {
            throw new IllegalArgumentException("seedArray 必须是空的");
        }
        this.seedArray = seedArray;
    }

    @Override
    public E[] toArray() {
        return toArray(seedArray);
    }

    @Override
    public String toString() {
        return "--" + super.toString() + "--";
    }
}
