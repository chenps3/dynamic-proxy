package chenps3.dynamicproxy.sample.ch02.protect;

import chenps3.dynamicproxy.sample.ch02.virtual.ICustomMap;

import java.util.function.BiConsumer;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public class SynchronizedCustomMap<K, V> implements ICustomMap<K, V> {

    private final ICustomMap<K, V> map;

    public SynchronizedCustomMap(ICustomMap<K, V> map) {
        this.map = map;
    }

    @Override
    public synchronized int size() {
        return map.size();
    }

    @Override
    public synchronized V get(K key) {
        return map.get(key);
    }

    @Override
    public synchronized V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public synchronized V remove(K key) {
        return map.remove(key);
    }

    @Override
    public synchronized void clear() {
        map.clear();
    }

    @Override
    public synchronized void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }
}
