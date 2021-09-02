package chenps3.dynamicproxy.sample.ch05.bettercollection;

import chenps3.dynamicproxy.sample.ch05.BetterArrayList;

/**
 * @Author chenguanhong
 * @Date 2021/9/1
 */
public class BetterArrayListDemo {

    public static void main(String[] args) {
        BetterArrayList<String> names = new BetterArrayList<>(new String[0]);
        names.add("Chen");
        names.add("Wang");
        names.add("Zhang");
        names.add("Huang");
        names.add("Xu");
        String[] nameArray = names.toArray();
        for (String name : nameArray) {
            System.out.println(name.toUpperCase());
        }
        System.out.println(names);
    }
}
