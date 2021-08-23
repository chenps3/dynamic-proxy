package chenps3.dynamicproxy.sample.ch02.remote;

/**
 * @Author chenguanhong
 * @Date 2021/8/17
 */
public class RealCanada implements ICanada {

    @Override
    public boolean canGetVisa(String name, boolean married, boolean rich) {
        return rich;
    }

}
