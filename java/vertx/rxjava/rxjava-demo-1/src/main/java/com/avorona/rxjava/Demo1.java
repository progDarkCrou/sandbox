package com.avorona.rxjava;

import rx.Observable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by avorona on 2016-08-03.
 */
public class Demo1 {
  public static void main(String[] args) throws InterruptedException {
    Observable<Integer> source = Observable.from(Arrays.asList(1, 2, 3, 4, 5))
        .delay(8, TimeUnit.SECONDS);

    source
        .map(i -> i + 10)
        .buffer(3)
        .forEach(integers -> System.out.println("Arrays: " + integers));

    source.forEach(integer -> System.out.println("Just listen value: " + integer));

    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
  }
}
