package chenps3;

import java.util.*;

/**
 * @Author chenguanhong
 * @Date 2021/8/13
 */
public class Test {

    public static void main(String[] args) {
        List<List<String>> l = new ArrayList<>();
        List<Integer> intList = List.of(42);
    }

    private <T> T[] toArray(T[] a) {
        if (a.length < 1) {
            return (T[]) Arrays.copyOf(null, 1, a.getClass());
        }
        return a;
    }
}
