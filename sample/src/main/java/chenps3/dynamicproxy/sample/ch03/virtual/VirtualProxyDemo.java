package chenps3.dynamicproxy.sample.ch03.virtual;

import chenps3.dynamicproxy.Proxies;
import chenps3.dynamicproxy.sample.ch02.virtual.CustomHashMap;
import chenps3.dynamicproxy.sample.ch02.virtual.ICustomMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @Author chenguanhong
 * @Date 2021/8/19
 */
public class VirtualProxyDemo {

    public static void main(String[] args) {
        ICustomMap<String, Integer> map = Proxies.virtualProxy(ICustomMap.class, (Supplier<CustomHashMap<String, Integer>>) CustomHashMap::new);
        System.out.println("创建虚拟Map对象");
        map.put("one", 1);
        map.put("life", 42);
        System.out.println("map.get(\"life\") = " + map.get("life"));
        System.out.println("map.size() = " + map.size());
        System.out.println("clearing map");
        map.clear();
        System.out.println("map.size() = " + map.size());

        Map<String,Integer> m = new HashMap<>();
        m.merge("chen",1,Integer::sum);
        System.out.println(m.get("chen"));
    }
}
