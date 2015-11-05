package example1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by avorona on 26.10.15.
 */
public class Application {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        int count = 10;

        IncrementalGenerator generator = new IncrementalGenerator();

        for (int i = 0; i < count; i++) {
            executorService.execute(new IntChecker(generator));
        }

        executorService.shutdown();
    }
}
