package com.avorona;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Created by avorona on 22.04.16.
 */
public class ApplicationContext implements Handler<AsyncResult<Vertx>> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private List<Verticle> toDeploy = new ArrayList<>();
    private List<Consumer<Vertx>> preDeploy = new ArrayList<>();

    private boolean isSequential;
    private boolean deployed;
    private boolean successfulDeploy;

    private Vertx vertx;

    @Override
    public void handle(AsyncResult<Vertx> clustered) {
        if (clustered.succeeded()) {
            this.vertx = clustered.result();
            if (toDeploy.size() == 0) {
                log.warn("No instances to deploy. Going to shut down.");
                closeAndExit(CloseStatus.NOTHING_TO_DEPLOY);
            }
            if (preDeploy.size() > 0) {
                log.info("Executing " + preDeploy.size() + " pre-deployment actions.");
                preDeploy.parallelStream().forEach(vc -> vc.accept(vertx));
            }

            if (isSequential) {
                log.info("Deploying {0} verticles sequentially", toDeploy.size());
                deploySequentially();
            } else {
                log.info("Deploying {0} verticles in parallel", toDeploy.size());
                deployParallel();
            }
        } else {
            log.error("Something happened while clustering: ", clustered.cause());
            log.warn("Going to close Vertx and shutdown");
            closeAndExit(CloseStatus.CLUSTERING_FAILED);
        }
    }

    public ApplicationContext addDeployment(Verticle verticle) {
        toDeploy.add(verticle);
        return this;
    }

    public ApplicationContext addPreDeploy(Consumer<Vertx> preDeploy) {
        this.preDeploy.add(preDeploy);
        return this;
    }

    public ApplicationContext setSequentialDeployment(boolean isSequential) {
        this.isSequential = isSequential;
        return this;
    }

    public boolean isSuccessfulDeploy() {
        return successfulDeploy;
    }

    private void deploySequentially() {
        Iterator<Verticle> iterator = toDeploy.iterator();
        Verticle first = iterator.next();
        vertx.deployVerticle(first, incrementalDeploy(first, iterator));
    }

    private Handler<AsyncResult<String>> incrementalDeploy(Verticle current, Iterator<Verticle> iterator) {
        return event -> {
            if (event.succeeded()) {
                log.debug("Successfully deployed verticle instance: {0}", event.result());
                if (iterator.hasNext()) {
                    Verticle next = iterator.next();
                    vertx.deployVerticle(next, incrementalDeploy(next, iterator));
                } else {
                    deployed = true;
                    successfulDeploy = true;
                    log.info("Sequential deployment of {0} verticles ended successfully", toDeploy.size());
                }
            } else {
                log.debug("Vertical {0} deployment failed with: {1}. Sequential deployment end" +
                        " on #{2} instance", current, event.result(), (toDeploy.indexOf(current) - 1));
            }
        };
    }

    private void deployParallel() {
        List<CompletableFuture<String>> futures = new CopyOnWriteArrayList<>();
        toDeploy.parallelStream().forEach(verticle -> {
            CompletableFuture<String> future = new CompletableFuture<>();
            vertx.deployVerticle(verticle, event -> {
                if (event.succeeded()) {
                    log.info("Verticle {0} successfully deployed in parallel", verticle);
                    future.complete(event.result());
                } else {
                    log.warn("Verticle {0} deployment failed with: {1}", verticle, event.cause());
                    future.completeExceptionally(event.cause());
                }
            });
            futures.add(future);
        });

        CompletableFuture[] completableFutures = futures.toArray(new CompletableFuture[futures.size()]);
        CompletableFuture.allOf(completableFutures).whenComplete((res, error) -> {
            deployed = true;
            if (error != null) {
                log.warn("Parallel deployment of {0} verticles ended with error: {1}", toDeploy.size(), error);
            } else {
                log.info("Parallel deployment of {0} verticles ended successfully", toDeploy.size());
            }
        });
    }

    private void closeAndExit(CloseStatus exitStatus) {
        vertx.close(event -> {
            if (event.succeeded()) {
                log.info("Vertx closed successfully");
            } else {
                log.info("Vertx failed to close with: {0}", event.cause());
            }
            System.exit(exitStatus.ordinal());
        });
    }

    public boolean isDeployed() {
        return deployed;
    }

    public enum CloseStatus {
        CLUSTERING_FAILED,
        NOTHING_TO_DEPLOY;
    }
}
