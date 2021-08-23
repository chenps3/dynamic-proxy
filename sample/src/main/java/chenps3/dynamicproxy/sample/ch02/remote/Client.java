package chenps3.dynamicproxy.ch02.remote;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public class Client {

    public static void main(String[] args) {
        ICanada canada = new CanadianEmbassy();
        boolean visaObtained = canada.canGetVisa("chen", true, false);
        System.out.println("没钱的chen: " + visaObtained);
        System.out.println("chen中了彩票");
        visaObtained = canada.canGetVisa("chen", true, true);
        System.out.println("有钱的chen: " + visaObtained);

    }
}
