package com.andriy.concurency_0;

/**
 * Created by crou on 02.11.15.
 */
public class RoundedBarier {

    private volatile int count;
    private volatile int awaiting = 0;

    public RoundedBarier(int count) {
        this.count = count;
    }

    public synchronized void await() throws InterruptedException {
        if (++awaiting == count) notifyAll();
            else wait();
        awaiting--;
    }
}
