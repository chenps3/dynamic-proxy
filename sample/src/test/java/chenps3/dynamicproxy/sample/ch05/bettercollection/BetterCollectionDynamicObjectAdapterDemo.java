package chenps3.dynamicproxy.sample.ch05.bettercollection;

import chenps3.dynamicproxy.sample.ch05.BetterCollection;
import chenps3.dynamicproxy.sample.ch05.BetterCollectionFactory;

import java.util.Collection;
import java.util.HashSet;

/**
 * @Author chenguanhong
 * @Date 2021/9/2
 */
public class BetterCollectionDynamicObjectAdapterDemo {

    public static void main(String[] args) {
        BetterCollection<String> names =
                BetterCollectionFactory.asBetterCollection(new HashSet<>(), new String[0]);
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
        //会报错
        ((Collection)names).add(42);
    }
}
