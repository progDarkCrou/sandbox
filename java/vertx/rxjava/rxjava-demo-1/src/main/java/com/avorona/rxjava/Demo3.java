package com.avorona.rxjava;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by avorona on 2016-08-03.
 */
public class Demo3 {

  public static void main(String[] args) throws InterruptedException {
    int waitersCount = 10;

    List<AwaitingThread> awaitings = new ArrayList<>(waitersCount);

    final Object blocker = new Object();

    for (int i = 0; i < waitersCount; i++) {
      awaitings.add(new AwaitingThread(blocker));
    }

    for (AwaitingThread thread : awaitings) {
      thread.start();
    }

    long wait = TimeUnit.SECONDS.toMillis(200);

    System.out.println(String.format("Waiting %d milliseconds", wait));
    Thread.sleep(wait);
    synchronized (blocker) {
      blocker.notifyAll();
    }
  }

  private static class AwaitingThread extends Thread {

    private final Object blocker;

    public AwaitingThread(Object blocker) {
      this.blocker = blocker;
    }

    @Override
    public void run() {
      while (true) {
        synchronized (blocker) {
          try {
            System.out.println(String.format("%s - waiting...", this.getName()));
            blocker.wait();
            System.out.println(String.format("%s - awake!", this.getName()));
            return;
          } catch (InterruptedException e) {
            e.printStackTrace();
            return;
          }
        }
      }
    }
  }
}
