package chenps3.dynamicproxy.sample.ch04.immutablecollection;

import java.util.Collection;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * @Author chenguanhong
 * @Date 2021/8/24
 */
public interface ImmutableCollection<E> extends ImmutableIterable<E> {
    int size();

    boolean isEmpty();

    boolean contains(Object o);

    Object[] toArray();

    <T> T[] toArray(T[] a);

    <T> T[] toArray(IntFunction<T[]> generator);

    boolean containsAll(Collection<?> c);

    Stream<E> stream();

    Stream<E> parallelStream();

    //接口默认方法
    default void printAll() {
        forEach(System.out::println);
    }

    //移除会改变集合的方法
//    boolean add(E e);
//    boolean remove(Object o);
//    boolean addAll(Collection<? extends E> c);
//    boolean removeAll(Collection<?> c);
//    boolean removeIf(Predicate<? super E> filter);
//    boolean retainAll(Collection<?> c);
//    void clear();
}
