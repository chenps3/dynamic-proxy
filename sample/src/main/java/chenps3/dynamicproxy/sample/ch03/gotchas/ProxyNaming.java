package chenps3.dynamicproxy.sample.ch03.gotchas;


import chenps3.dynamicproxy.Proxies;
import chenps3.dynamicproxy.handler.BaseComponent;

/**
 * @Author chenguanhong
 * @Date 2021/8/20
 */
public class ProxyNaming {

    public interface PublicNotExported {
        void open();
    }

    interface Hidden {
        void mystery();
    }

    public static void main(String[] args) {
        show(BaseComponent.class);
        show(PublicNotExported.class);
        show(Hidden.class);
    }

    private static void show(Class<?> intf) {
        System.out.println(Proxies.simpleProxy(intf, null).getClass());
    }
}
