import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by avorona on 26.10.15.
 */
public class IncrementalGenerator extends IntGenerator {

    private volatile int i = 0;

    private Lock lock = new ReentrantLock();

    @Override
    public int generateEven() {
        lock.lock();
        try {
            ++i;
            Thread.yield();
            ++i;
        } finally {
            lock.unlock();
        }
        return i;
    }
}
