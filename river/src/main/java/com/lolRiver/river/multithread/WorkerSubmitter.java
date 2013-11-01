package com.lolRiver.river.multithread;

import com.google.common.collect.ImmutableMap;
import com.lolRiver.config.ConfigMap;
import com.lolRiver.river.multithread.workerStarters.WorkerStarter;
import com.lolRiver.river.multithread.workers.Worker;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
/**
 * @author mxia (mxia@lolRiver.coma)
 *         9/29/13
 */

public abstract class WorkerSubmitter implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(WorkerSubmitter.class.getName());
    private static final long THREAD_JOIN_TIMEOUT = 300L; // seconds

    private ExecutorService executor;
    private boolean shouldExit = false;
    private boolean hasStarted = false;
    private boolean startNow;
    private Thread thread;
    private long loopInterval;
    private ConfigMap configMap;

    protected List<WorkerStarter> workerStarters;

    //@PostConstruct
    public void setup() {
        setup(false);
    }

    //@PreDestroy
    public void teardown() {
        if (!hasStarted) {
            LOGGER.warn("Trying to stop when it hasn't started");
        } else {
            shutdownExecutorService(executor, "WorkerSubmitter Thread Pool Executor");
            shouldExit = true;
            hasStarted = false;
        }
    }

    public void setup(boolean startNow) {
        if (hasStarted) {
            LOGGER.warn("Trying to start when it's already started");
        } else {
            start(startNow);
        }
    }

    public void start(boolean startNow) {
        init(startNow);
        thread = new Thread(this);
        thread.start();
        hasStarted = true;
    }

    public void init(boolean startNow) {
        this.configMap = configMap();
        this.loopInterval = configMap.getLong("loop_interval");
        this.startNow = startNow;

        int maxPoolSize = configMap.getInteger("maximum_pool_size");
        LOGGER.info("Constructing and starting: " + this + "with max pool size = " + maxPoolSize);
        executor = Executors.newFixedThreadPool(maxPoolSize);
        shouldExit = false;
    }

    public abstract ConfigMap configMap();

    private void shutdownExecutorService(ExecutorService executorService, String label) {
        LOGGER.info("Shutting down executor service");
        int retryCount = 0;
        executorService.shutdown();
        try {
            //awaitTermination returns false on timeout
            while (!executorService.awaitTermination(THREAD_JOIN_TIMEOUT, TimeUnit.SECONDS)) {
                retryCount += 1;
                LOGGER.warn("Timed out after waiting for " + label + " to finish terminating. Retrying." +
                        ImmutableMap.of("timeout(s)", String.valueOf(THREAD_JOIN_TIMEOUT), "retry_count", String.valueOf(retryCount)));
            }
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while waiting on " + label + ": ", e);
            executorService.shutdownNow();
        }
    }

    public void run() {
        LOGGER.info("WorkerSubmitter is starting");

        while (!shouldExit) {
            boolean shouldStart = false;
            if (this.startNow) {
                shouldStart = true;
                this.startNow = false;
            }
            for (WorkerStarter workerStarter : workerStarters) {
                LOGGER.debug("Checking if " + workerStarter + " can start");
                if (shouldStart || workerStarter.canStart()) {
                    LOGGER.info("Starting " + workerStarter);
                    workerStarter.start();
                    for (Worker worker : workerStarter.workersToStart()) {
                        try {
                            executor.submit(worker);
                        } catch (RejectedExecutionException e) {
                            LOGGER.error(" ThreadPoolExecutor threads and queue queue full." +
                                    " Sleeping for " + loopInterval + "ms, then will try again");
                        }
                    }
                }
            }

            try {
                Thread.sleep(loopInterval);
            } catch (InterruptedException ignored) {
            }
        }

        LOGGER.info("WorkerSubmitter is exiting");
    }

    public boolean isStartNow() {
        return startNow;
    }

    public void setStartNow(boolean startNow) {
        this.startNow = startNow;
    }
}
