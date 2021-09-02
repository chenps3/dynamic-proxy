package chenps3.dynamicproxy.sample.ch05;

import java.util.ArrayList;

/**
 * 手写class适配器1
 * ArrayList的toArray方法返回的是Object[]类型
 * java 5 后支持协变返回类型，所以子类可以返回Object[]的子类型
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public class BetterArrayList<E> extends ArrayList<E> {

    private final E[] seedArray;

    public BetterArrayList(E[] seedArray) {
        if (seedArray.length != 0) {
            throw new IllegalArgumentException("seedArray 必须是空的");
        }
        this.seedArray = seedArray;
    }

    @Override
    public E[] toArray() {
        //创建一个有类型的数组的最快方式就是从集合创建
        //https://shipilev.net/blog/2016/arrays-wisdom-ancients/
        return toArray(seedArray);
    }

    @Override
    public String toString() {
        return "--" + super.toString() + "--";
    }
}
