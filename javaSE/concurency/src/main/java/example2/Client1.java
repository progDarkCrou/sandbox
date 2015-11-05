package example2;

import java.util.concurrent.TimeUnit;

/**
 * Created by avorona on 03.11.15.
 */
public class Client1 extends Client implements Runnable {

    public Client1(Account account) {
        super(account);
    }


    public void run() {
        while (!Thread.interrupted()) {
            putAndCheck(10);
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 10));
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
