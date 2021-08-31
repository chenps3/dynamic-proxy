package chenps3.dynamicproxy.sample.ch04.immutablecollection;

import java.util.Arrays;

/**
 * @Author chenguanhong
 * @Date 2021/8/24
 */
public class HandcodedFilterDemo {

    public static void main(String[] args) {
        ImmutableCollection<String> names = new HandcodedFilter<>(Arrays.asList("Peter", "Paul", "Mary"));
        System.out.println(names);
        System.out.println("Is Mary in? " + names.contains("Mary"));
        System.out.println("Are there names? " + !names.isEmpty());
        System.out.println("Printing the names:");
        names.forEach(System.out::println);
        System.out.println("Class: " + names.getClass().getSimpleName());
        names.printAll();
    }
}
