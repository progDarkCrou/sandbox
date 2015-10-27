/**
 * Created by avorona on 26.10.15.
 */
public abstract class IntGenerator {

    private volatile boolean canceled = false;

    public abstract int generateEven();

    public boolean isCanceled() {
        return canceled;
    }

    public void cancel() {
        this.canceled = true;
    }
}
