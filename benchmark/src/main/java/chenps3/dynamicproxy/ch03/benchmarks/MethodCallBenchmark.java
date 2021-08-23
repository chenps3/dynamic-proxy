package chenps3.dynamicproxy.ch03.benchmarks;

import chenps3.dynamicproxy.Proxies;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * 1 dicrectCall 直接调用RealWorker
 * 2 staticProxy 静态代理，调用ProxyWorker，它会委托到RealWorker
 * 3 dynamicProxyThenDirectCall 创建Worker动态代理，直接委托给RealWorker
 * 4 dynamicProxyThenReflectiveCall(turbo) 创建Worker动态代理，通过反射委托给RealWorker，开启turbo优化
 * 5 dynamicProxyThenReflectiveCall(no turbo) 同4但不开启turbo优化
 *
 * @Author chenguanhong
 * @Date 2021/8/21
 */
@Fork(value = 3, jvmArgsAppend = "-XX:UseParallelGC")
@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 10, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class MethodCallBenchmark {
    //dicrectCall
    private final RealWorker realWorker = new RealWorker();
    //staticProxy
    private final Worker staticProxy = new ProxyWorker();
    //动态代理
    private final Worker dynamicProxyThenDirectCallIncrement =
            Proxies.castProxy(Worker.class,
                    (proxy, method, args) -> realWorker.increment());
    private final Worker dynamicProxyThenDirectCallConsumeCPU =
            Proxies.castProxy(Worker.class,
                    (proxy, method, args) -> {
                        realWorker.consumeCPU();
                        return null;
                    });
    private final Worker dynamicProxyThenReflectiveCall =
            Proxies.castProxy(Worker.class,
                    (proxy, method, args) ->
                            method.invoke(realWorker, args));

    @Benchmark
    public long dicrectCallIncrement(){
        return realWorker.increment();
    }

    @Benchmark
    public long staticProxyIncrement(){
        return staticProxy.increment();
    }

    @Benchmark
    public long dynamicProxyThenDirectCallIncrement(){
        return dynamicProxyThenDirectCallIncrement.increment();
    }

    @Benchmark
    public long dynamicProxyThenReflectiveCallIncrement(){
        return dynamicProxyThenReflectiveCall.increment();
    }

    @Benchmark
    public void directCallConsumeCPU(){
        realWorker.consumeCPU();
    }

    @Benchmark
    public void staticProxyConsumeCPU(){
        staticProxy.consumeCPU();
    }

    @Benchmark
    public void dynamicProxyThenDirectCallConsumeCPU(){
        dynamicProxyThenDirectCallConsumeCPU.consumeCPU();
    }

    @Benchmark
    public void dynamicProxyThenReflectiveCallConsomeCPU(){
        dynamicProxyThenReflectiveCall.consumeCPU();
    }
}
