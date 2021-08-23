package chenps3.dynamicproxy.ch03.enhancedstream;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author chenguanhong
 * @Date 2021/8/20
 */
public class EnhancedStreamDemo {

    public static void main(String[] args) {
        System.out.println("普通的ArrayDeque clone() 方法:");
        Stream.of(ArrayDeque.class.getMethods())
                .filter(m -> m.getName().equals("clone"))
                .distinct()
                .forEach(EnhancedStreamDemo::print);
        System.out.println();
        System.out.println("Distinct ArrayDeque:");
        EnhancedStream.of(ArrayDeque.class.getMethods())
                .filter(m -> m.getName().equals("clone"))
                .distinct(HASH_CODE, EQUALS, MERGE)
                .forEach(EnhancedStreamDemo::print);
        System.out.println();
        System.out.println("普通的 ConcurrentSkipListSet:");
        Stream.of(ConcurrentSkipListSet.class.getMethods())
                .filter(m -> m.getName().contains("Set"))
                .distinct()
                .forEach(EnhancedStreamDemo::print);
        System.out.println();
        System.out.println("Distinct ConcurrentSkipListSet:");
        EnhancedStream.of(ConcurrentSkipListSet.class.getMethods())
                .filter(m -> m.getName().contains("Set"))
                .distinct(HASH_CODE, EQUALS, MERGE)
                .forEach(EnhancedStreamDemo::print);
    }

    public static final ToIntFunction<Method> HASH_CODE =
            m -> m.getName().hashCode() + m.getParameterCount();
    public static final BiPredicate<Method, Method> EQUALS =
            (m1, m2) -> m1.getName().equals(m2.getName()) &&
                    m1.getParameterCount() == m2.getParameterCount() &&
                    Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());
    public static final BinaryOperator<Method> MERGE =
            (m1, m2) -> {
                if (m1.getReturnType().isAssignableFrom(m2.getReturnType())) {
                    return m2;
                }
                if (m2.getReturnType().isAssignableFrom(m1.getReturnType())) {
                    return m1;
                }
                throw new IllegalArgumentException("返回类型冲突：" + m1.getReturnType().getCanonicalName() + " and " + m2.getReturnType().getCanonicalName());
            };


    private static void print(Method m) {
        String t = Stream.of(m.getParameterTypes())
                .map(Class::getSimpleName)
                //前缀：返回值类型 + 方法名 + (
                //后缀：）
                .collect(Collectors.joining(
                        ", ",
                        m.getReturnType().getSimpleName() + " " + m.getName() + "(",
                        ")"));
        System.out.println(t);
    }
}
