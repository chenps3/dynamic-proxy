package chenps3.dynamicproxy.ch02.equals;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */

import chenps3.dynamicproxy.ch02.virtual.ICustomMap;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class EqualsInCustomHashMap<K, V> implements ICustomMap<K, V> {

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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ICustomMap)) {
            return false;
        }
        if (o instanceof EqualsInCustomHashMap) {
            EqualsInCustomHashMap<?, ?> that = (EqualsInCustomHashMap<?, ?>) o;
            return this.map.equals(that.map);
        }
        //如果用this.equals(o)，会无限递归
        return o.equals(this);
    }

    @Override
    public final int hashCode() {
        return map.hashCode();
    }
}
