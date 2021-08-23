package chenps3.dynamicproxy.ch02.virtual;

import java.util.function.BiConsumer;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public interface ICustomMap<K, V> {
    int size();

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    void clear();

    void forEach(BiConsumer<? super K, ? super V> action);
}
