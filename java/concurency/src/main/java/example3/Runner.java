package example3;

import java.util.concurrent.TimeUnit;

/**
 * Created by avorona on 03.11.15.
 */
public class Runner implements Runnable {

    private static boolean run = true;
    private static int num;

    public void run() {
        while (run && !Thread.interrupted() && !Thread.currentThread().isInterrupted()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Ending run method with number = " + num + " ...");
    }


    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(Runner::new);

        thread.start();

        TimeUnit.SECONDS.sleep(5);
        num = 100;
        run = false;

        TimeUnit.SECONDS.sleep(1);
        if (thread.isAlive()) {
            TimeUnit.SECONDS.sleep(3);
            System.out.println("Interrupting run method");
            thread.interrupt();
        }

    }
}
