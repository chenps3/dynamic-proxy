package chenps3.dynamicproxy.sample.ch05;

import java.util.Collection;
import java.util.Iterator;

/**
 * @Author chenguanhong
 * @Date 2021/9/2
 */
public class BetterCollectionObjectAdapter<E> implements BetterCollection<E> {

    private final Collection<E> adaptee;

    private final E[] seedArray;

    public BetterCollectionObjectAdapter(Collection<E> adaptee, E[] seedArray) {
        if (seedArray.length != 0) {
            throw new IllegalArgumentException("seedArray 必须是空的");
        }
        this.adaptee = adaptee;
        this.seedArray = seedArray;
    }

    @Override
    public E[] toArray() {
        return adaptee.toArray(seedArray);
    }

    @Override
    public String toString() {
        return "--" + adaptee.toString() + "--";
    }

    @Override
    public int size() {
        return adaptee.size();
    }

    @Override
    public boolean isEmpty() {
        return adaptee.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return adaptee.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return adaptee.iterator();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return adaptee.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return adaptee.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return adaptee.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return adaptee.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return adaptee.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return adaptee.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return adaptee.retainAll(c);
    }

    @Override
    public void clear() {
        adaptee.clear();
    }
}
