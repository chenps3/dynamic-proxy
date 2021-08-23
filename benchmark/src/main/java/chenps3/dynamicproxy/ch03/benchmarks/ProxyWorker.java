package chenps3.dynamicproxy.ch03.benchmarks;

/**
 * @Author chenguanhong
 * @Date 2021/8/21
 */
public class ProxyWorker implements Worker {

    private final RealWorker worker = new RealWorker();

    @Override
    public long increment() {
        return worker.increment();
    }

    @Override
    public void consumeCPU() {
        worker.consumeCPU();
    }
}
