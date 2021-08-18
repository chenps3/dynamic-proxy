package chenps3.dynamicproxy.ch02.virtual;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public class VirtualCustomMap<K, V> implements ICustomMap<K, V> {

    private final Supplier<ICustomMap<K, V>> supplier;

    private ICustomMap<K, V> realMap;

    public VirtualCustomMap(Supplier<ICustomMap<K, V>> supplier) {
        this.supplier = supplier;
    }

    private ICustomMap<K, V> getRealMap() {
        if (realMap == null) {
            return supplier.get();
        }
        return realMap;
    }

    @Override
    public int size() {
        return getRealMap().size();
    }

    @Override
    public V get(K key) {
        return getRealMap().get(key);
    }

    @Override
    public V put(K key, V value) {
        return getRealMap().put(key, value);
    }

    @Override
    public V remove(K key) {
        return getRealMap().remove(key);
    }

    @Override
    public void clear() {
        getRealMap().clear();
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        getRealMap().forEach(action);
    }
}
