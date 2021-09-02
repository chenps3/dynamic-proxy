package chenps3.dynamicproxy.sample.ch05;

import chenps3.dynamicproxy.Proxies;

/**
 * 定义了public接口后，length方法对外部可见，可以正确适配
 * @Author chenguanhong
 * @Date 2021/9/2
 */
public class AdapterPuzzle2 {

    public interface LengthProvider {
        int length();
    }

    public static void main(String[] args) {
        CharSequence seq = Proxies.adapt(CharSequence.class, "Good Morning", new LengthProvider() {
            @Override
            public int length() {
                return 42;
            }

            @Override
            public String toString() {
                return "chenps3";
            }
        });
        System.out.println(seq + " " + seq.length());
    }
}
