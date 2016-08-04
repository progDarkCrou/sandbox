package com.avorona.rxjava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by avorona on 2016-08-03.
 */
public class Demo2 {
  public static void main(String[] args) throws InterruptedException {
    int tickerCount = 100;
    NumberBuffer buffer = new NumberBuffer();
    ArrayList<Ticker> tickers = new ArrayList<>(tickerCount);

    for (int i = 0; i < tickerCount; i++) {
      tickers.add(new Ticker(buffer));
    }

    for (Ticker ticker : tickers) {
      ticker.start();
    }

    System.out.println("Waiting...");
    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
    System.out.println("Stopping tickers");

    for (Ticker ticker : tickers) {
      ticker.halt();
    }

    System.out.println(buffer.statistic());
  }

  private static class NumberBuffer {

    private ReentrantLock lock = new ReentrantLock();

    private Map<Integer, Integer> counter = new HashMap<>();

    public void add(int number) {
      String threadName = Thread.currentThread().getName();
      System.out.println(String.format("Setting %d from thread %s", number, threadName));

      try {
        lock.lock();
        counter.put(number, counter.get(number) != null ? counter.get(number) + 1 : 1);
      } finally {
        lock.unlock();
      }
    }

    public String statistic() {
      StringBuilder builder = new StringBuilder();

      for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
        builder.append(entry.getKey());
        builder.append("=");
        builder.append(entry.getValue());
        builder.append("\n");
      }

      return builder.toString();
    }
  }

  private static class Ticker extends Thread {

    private boolean stop;

    private int start;

    private NumberBuffer buffer;

    public Ticker(NumberBuffer buffer) {
      this.buffer = buffer;
    }

    @Override
    public void run() {
      while (!stop) {
        buffer.add(start++);
        try {
          sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
          break;
        }
      }
    }

    public void halt() {
      this.stop = true;
    }
  }
}
