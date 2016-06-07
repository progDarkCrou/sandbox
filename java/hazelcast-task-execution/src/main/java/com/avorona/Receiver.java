package com.avorona;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Created by avorona on 13.05.16.
 */
public class Receiver {

    public static void main(String[] args) throws InterruptedException {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();

        Thread.sleep(60 * 1000);
        instance.shutdown();
    }
}
