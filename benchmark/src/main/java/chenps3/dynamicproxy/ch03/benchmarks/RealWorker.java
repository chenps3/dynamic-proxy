package chenps3.dynamicproxy.ch03.benchmarks;

import org.openjdk.jmh.infra.Blackhole;

/**
 * @Author chenguanhong
 * @Date 2021/8/21
 */
public class RealWorker implements Worker {

    private long counter = 0;

    @Override
    public long increment() {
        return counter++;
    }

    @Override
    public void consumeCPU() {
        Blackhole.consumeCPU(2);
    }
}
