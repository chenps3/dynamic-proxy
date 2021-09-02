package chenps3.dynamicproxy.sample.ch05;

import chenps3.dynamicproxy.Proxies;

/**
 * 这个程序输出字符串是chenps3，但长度是Good Morning的长度12
 * 匿名内部类是package私有的，所以他的length方法对外部不可见
 * 当调用length()时，实际上调用的是String的length方法，于是得到Good Morning的长度12；
 * toString方法继承自public class Object，所以对外部可见
 * @Author chenguanhong
 * @Date 2021/9/2
 */
public class AdapterPuzzle1 {

    public static void main(String[] args) {
        CharSequence seq = Proxies.adapt(CharSequence.class, "Good Morning", new Object() {
            @Override
            public String toString() {
                return "chenps3";
            }

            public int length() {
                return 42;
            }
        });
        System.out.println(seq + " " + seq.length());
    }
}
