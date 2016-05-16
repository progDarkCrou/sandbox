package com.avorona;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by avorona on 13.05.16.
 */
public class Sender {
    public static void main(String[] args) {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        IExecutorService executorService = instance.getExecutorService("task-executor");

        List<CompletableFuture<String>> collect = IntStream.range(0, 10)
                .boxed()
                .map(i -> executorService.submit(new Task("first node #" + i)))
                .map(sf -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return sf.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .collect(Collectors.toList());

        collect.forEach(f -> f.thenAccept(System.out::println));
    }
}
