package chenps3.dynamicproxy.sample.ch02.virtual;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */

import java.util.HashMap;
import java.util.function.BiConsumer;

public class CustomHashMap<K, V> implements ICustomMap<K, V> {

    private final HashMap<K, V> map = new HashMap<>();

    {
        System.out.println("CustomHashMap构造完成");
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
        return map.put(key, value);
    }

    @Override
    public V remove(K key) {
        return map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }
}
