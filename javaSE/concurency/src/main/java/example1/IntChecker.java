package example1;

/**
 * Created by avorona on 26.10.15.
 */
public class IntChecker extends Thread {

    private static int i = 0;

    private IntGenerator generator;

    public IntChecker(IntGenerator generator) {
        super();
        this.generator = generator;
        System.out.println("Checker " + i++ + " started");
    }

    @Override
    public void run() {
        int i = 0;
        while (!generator.isCanceled() && !isInterrupted()) {
            int even = generator.generateEven();
            if (even % 2 != 0) {
                System.out.println(even + ": not even. Iteration " + i);
                generator.cancel();
            }
            i++;
        }
    }
}
