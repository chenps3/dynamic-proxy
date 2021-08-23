package chenps3.dynamicproxy.sample.ch03.enhancedstream;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author chenguanhong
 * @Date 2021/8/19
 */
public class EnhancedStreamHandler<T> implements InvocationHandler, Serializable {

    private Stream<T> delegate;

    public EnhancedStreamHandler(Stream<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getReturnType() == (EnhancedStream.class)) {
            if (method.equals(ENHANCED_DISTINCT)) {
                distinct((ToIntFunction<T>) args[0], (BiPredicate<T, T>) args[1], (BinaryOperator<T>) args[2]);
            } else if (method.equals(ENHANCED_DISTINCT_WITH_KEY)) {
                distinct((Function<T, ?>) args[0], (BinaryOperator<T>) args[1]);
            } else {
                Method enhancedMethod = methodMap.get(method);
                this.delegate = (Stream<T>) enhancedMethod.invoke(delegate, args);
            }
            return proxy;
        } else {
            return method.invoke(delegate, args);
        }
    }

    private void distinct(ToIntFunction<T> hashCode, BiPredicate<T, T> equals, BinaryOperator<T> merger) {
        distinct(t -> new Key<>(t, hashCode, equals), merger);
    }

    private void distinct(Function<T, ?> keyGen, BinaryOperator<T> merger) {
        delegate = delegate.collect(Collectors.toMap(keyGen, x -> x, merger, LinkedHashMap::new))
                .values().stream();
    }

    private static final class Key<E> {
        private final E e;
        private final ToIntFunction<E> hashCode;
        private final BiPredicate<E, E> equals;

        public Key(E e, ToIntFunction<E> hashCode, BiPredicate<E, E> equals) {
            this.e = e;
            this.hashCode = hashCode;
            this.equals = equals;
        }

        @Override
        public int hashCode() {
            return hashCode.applyAsInt(e);
        }

        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Key)) {
                return false;
            }
            Key<E> that = (Key<E>) o;
            return equals.test(this.e, that.e);
        }
    }

    private static final Method ENHANCED_DISTINCT;
    private static final Method ENHANCED_DISTINCT_WITH_KEY;

    static {
        try {
            ENHANCED_DISTINCT = EnhancedStream.class.getMethod(
                    "distinct", ToIntFunction.class, BiPredicate.class, BinaryOperator.class
            );
            ENHANCED_DISTINCT_WITH_KEY = EnhancedStream.class.getMethod(
                    "distinct", Function.class, BinaryOperator.class
            );
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }

    //EnhancedStream增强的方法与Stream名称一致
    private static Method getEnhancedMethod(Method m) {
        try {
            return EnhancedStream.class.getMethod(m.getName(), m.getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }

    private static final Map<Method, Method> methodMap =
            Stream.of(Stream.class.getMethods())
                    .filter(i -> !Modifier.isStatic(i.getModifiers()))
                    .collect(Collectors.toUnmodifiableMap(
                            EnhancedStreamHandler::getEnhancedMethod, x -> x
                    ));
}
