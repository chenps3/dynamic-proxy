package chenps3.dynamicproxy;

import chenps3.dynamicproxy.util.VTable;
import chenps3.dynamicproxy.util.VTables;

import java.util.ArrayDeque;
import java.util.List;

/**
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public class VTablesDemo {

    public static void main(String[] args) {
        VTable vt = VTables.newVTable(ArrayDeque.class, List.class);
        System.out.println();
        //输出同时出现在ArrayDeque和List的方法
        vt.stream().forEach(System.out::println);
    }
}
