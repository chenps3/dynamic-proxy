package chenps3.dynamicproxy.sample.ch04.immutablecollection;

import java.util.Collection;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * 手写的过滤器，移除Collection接口里可以修改的方法
 * @Author chenguanhong
 * @Date 2021/8/24
 */
public class HandcodedFilter<E> implements ImmutableCollection<E> {

    private final Collection<E> c;

    public HandcodedFilter(Collection<E> c) {
        this.c = c;
    }

    @Override
    public int size() {
        return c.size();
    }

    @Override
    public boolean isEmpty() {
        return c.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return c.contains(o);
    }

    @Override
    public Object[] toArray() {
        return c.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return c.toArray(a);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return c.toArray(generator);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.c.containsAll(c);
    }

    @Override
    public Stream<E> stream() {
        return c.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return c.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        c.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return c.spliterator();
    }
}
