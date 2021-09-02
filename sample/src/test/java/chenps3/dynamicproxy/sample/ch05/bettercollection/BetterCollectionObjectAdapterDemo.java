package chenps3.dynamicproxy.sample.ch05.bettercollection;

import chenps3.dynamicproxy.sample.ch05.BetterCollection;
import chenps3.dynamicproxy.sample.ch05.BetterCollectionObjectAdapter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * object适配器，适配ConcurrentHashMap.newKeySet()
 *
 * @Author chenguanhong
 * @Date 2021/9/2
 */
public class BetterCollectionObjectAdapterDemo {

    public static void main(String[] args) {
        BetterCollection<String> names = new BetterCollectionObjectAdapter<>(ConcurrentHashMap.newKeySet(), new String[0]);
        names.add("Chen");
        names.add("Wang");
        names.add("Zhang");
        names.add("Huang");
        names.add("Xu");
        String[] nameArray = names.toArray();
        for (String s : nameArray) {
            System.out.println(s.toUpperCase());
        }
        System.out.println(names);
    }
}
