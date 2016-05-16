package com.avorona;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * Created by avorona on 13.05.16.
 */
public class Task implements Callable<String>, HazelcastInstanceAware, Serializable {

    private String name;
    private HazelcastInstance hazelcast;

    public Task(String name) {
        this.name = name;
    }

    public String call() throws Exception {
        System.out.println("Executing task for the " + name);
        return name + hazelcast.getCluster().getLocalMember().getUuid();
    }

    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcast = hazelcastInstance;
    }
}
