package chenps3.dynamicproxy.sample.ch05;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * class适配器：
 * @Author chenguanhong
 * @Date 2021/9/1
 */
public interface BetterCollection<E> extends Collection<E> {

    @Override
    E[] toArray();

    default void forEachFiltered(Predicate<? super E> predicate, Consumer<? super E> action) {
        Objects.requireNonNull(predicate, "predicate==null");
        Objects.requireNonNull(action, "action==null");
        for (E e : this) {
            if (predicate.test(e)) {
                action.accept(e);
            }
        }
    }
}
