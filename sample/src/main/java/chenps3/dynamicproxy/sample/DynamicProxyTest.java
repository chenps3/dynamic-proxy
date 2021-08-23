package chenps3.dynamicproxy.sample;

import java.lang.reflect.Proxy;

/**
 * @Author chenguanhong
 * @Date 2020/12/15
 */
public class DynamicProxyTest {

    interface IA {
        String getHelloName();
    }

    public static void main(String[] arges) throws Exception {
        IA ia = (IA) createObject(IA.class.getName() + "$getHelloName=Abc");
        System.out.println(ia.getHelloName()); //getHelloName匹配，输出Abc
        ia = (IA) createObject(IA.class.getName() + "$getTest=Bcd");
        System.out.println(ia.getHelloName()); //getTest不匹配，输出null
    }

    //getHelloName只是个示例，不可以写死判断

    //请实现方法createObject
    public static Object createObject(String str) throws Exception {
        String className = str.substring(0, str.lastIndexOf("$"));
        String methodName = str.substring(str.lastIndexOf("$") + 1, str.lastIndexOf("="));
        String arg = str.substring(str.lastIndexOf("=") + 1);
        Class<?> cls = Class.forName(className);
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, (proxy, method, args) -> {
            if (methodName.equals(method.getName())) {
                return arg;
            }
            return null;
        });
    }

}

