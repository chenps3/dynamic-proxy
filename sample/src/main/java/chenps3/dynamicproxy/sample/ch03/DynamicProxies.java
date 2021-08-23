package chenps3.dynamicproxy.sample.ch03;

import chenps3.dynamicproxy.Proxies;

/**
 * @Author chenguanhong
 * @Date 2021/8/19
 */
public class DynamicProxies {

    public static void main(String[] args) throws Exception {
        ISODateParser proxyParser = Proxies.simpleProxy(ISODateParser.class, new RealISODateParser());
        System.out.println(proxyParser);
        System.out.println(proxyParser.parse("2021-08-19"));
        System.out.println(proxyParser.parse("2021-04-31"));
    }
}
