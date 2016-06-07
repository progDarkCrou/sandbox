package example2;

import java.util.concurrent.TimeUnit;

/**
 * Created by avorona on 03.11.15.
 */
public class Bank {

    public static void main(String[] args) throws InterruptedException {
        Account account = new Account(100);

        Thread one = new Thread(new Client1(account));
        Thread two = new Thread(new Client2(account));

        one.start();
        two.start();

        TimeUnit.SECONDS.sleep(10);

        System.out.println("Canceling bank...");
        one.interrupt();
        two.interrupt();
    }
}
