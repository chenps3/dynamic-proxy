package chenps3.dynamicproxy.util;

/**
 * VTable的静态工厂
 *
 * @Author chenguanhong
 * @Date 2021/8/31
 */
public class VTables {

    private VTables() {
    }

    /**
     * 构造映射默认接口方法的VTable
     */
    public static VTable newDefaultMethodVTable(Class<?> clazz) {
        return newVTableBuilder(clazz, clazz)
                .excludeObjectMethods()
                .includeDefaultMethods()
                .build();
    }

    /**
     * 普通VTable
     */
    public static VTable newVTable(Class<?> receiver, Class<?>... targets) {
        return newVTableBuilder(receiver, targets).build();
    }

    /**
     * 排除Object类的基本方法
     */
    public static VTable newVTableExcludingObjectMethods(Class<?> receiver, Class<?>... targets) {
        return newVTableBuilder(receiver, targets)
                .excludeObjectMethods()
                .build();
    }

    private static VTable.Builder newVTableBuilder(Class<?> receiver, Class<?>... targets) {
        VTable.Builder builder = new VTable.Builder(receiver);
        for (Class<?> target : targets) {
            builder.addTargetInterface(target);
        }
        return builder;
    }


}
