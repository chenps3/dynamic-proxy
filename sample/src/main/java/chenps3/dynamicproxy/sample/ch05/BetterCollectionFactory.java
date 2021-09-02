package chenps3.dynamicproxy.sample.ch05;

import chenps3.dynamicproxy.Proxies;

import java.util.Collection;

/**
 * @Author chenguanhong
 * @Date 2021/9/2
 */
public class BetterCollectionFactory {

    public static <E> BetterCollection<E> asBetterCollection(Collection<E> adaptee, E[] seedArray) {
        return Proxies.adapt(BetterCollection.class, adaptee, new AdaptationObject<>(adaptee, seedArray));
    }

    /**
     * 这个类包含了要适配的方法
     */
    public static class AdaptationObject<E> {
        private final Collection<E> adaptee;
        private final E[] seedArray;

        public AdaptationObject(Collection<E> adaptee, E[] seedArray) {
            this.adaptee = adaptee;
            this.seedArray = seedArray;
        }

        public E[] toArray() {
            return adaptee.toArray(seedArray);
        }

        @Override
        public String toString() {
            return "--" + adaptee + "--";
        }

        //
        public boolean add(E e) {
            //getComponentType：返回数组元素的类型
            var type = seedArray.getClass().getComponentType();
            if (!type.isInstance(e)) {
                throw new ClassCastException("试图把一个" + e.getClass() + "值插入类型为" + type + "的集合");
            }
            return adaptee.add(e);
        }
    }
}
