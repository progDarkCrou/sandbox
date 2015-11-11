package com.andriy.concurency_0;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by crou on 02.11.15.
 */
public class Task implements Runnable {

    private static int count;

    private RoundedBarier barier;
    private int i;

    {
        i = count++;
    }

    public Task (RoundedBarier barier) {
        this.barier = barier;
    }

    public void run() {
        try {
            TimeUnit.SECONDS.sleep(10);
            barier.await();
            System.out.println("[" + i + "] Task ended.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int taskCount = Runtime.getRuntime().availableProcessors();

        ExecutorService executor = Executors.newFixedThreadPool(taskCount);

        RoundedBarier barier = new RoundedBarier(taskCount);

        for (int i = 0; i < taskCount * 2; i++) {
            executor.execute(new Task(barier));
        }

        executor.shutdown();
    }
}
