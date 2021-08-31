package chenps3.dynamicproxy.sample.ch04.immutablecollection;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @Author chenguanhong
 * @Date 2021/8/23
 */
public interface ImmutableIterable<E> {
    void forEach(Consumer<? super E> action);
    Spliterator<E> spliterator();

//    iterator包含remove方法，我们希望集合不可变，所以移除掉iterator
//    Iterator<E> iterator();
}
