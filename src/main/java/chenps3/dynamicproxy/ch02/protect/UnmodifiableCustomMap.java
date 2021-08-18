package chenps3.dynamicproxy.ch02.protect;

import chenps3.dynamicproxy.ch02.virtual.ICustomMap;

import java.util.function.BiConsumer;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public class UnmodifiableCustomMap<K, V> implements ICustomMap<K, V> {

    private final ICustomMap<K, V> map;

    public UnmodifiableCustomMap(ICustomMap<K, V> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("unmodifiable");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("unmodifiable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("unmodifiable");
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }
}
