/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util.thread;

import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;
import kunlun.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * The executor service cleaner.
 * @author Kahle
 */
public class ExecutorServiceCleaner implements Runnable {
    private final List<ExecutorService> threadPoolList;
    private Long    awaitTerminationTime = 200L;
    private Boolean ignoreException = true;
    private Boolean shutdownNow = false;

    private void awaitTermination(ExecutorService executorService) {
        try {
            if (executorService.awaitTermination(awaitTerminationTime, MILLISECONDS)) {
                return;
            }
            // If is "shutdownNow", give up try again.
            if (shutdownNow) { return; }
            executorService.shutdownNow();
            // Give up try again.
            executorService.awaitTermination(awaitTerminationTime, MILLISECONDS);
        }
        catch (InterruptedException e) {
            if (!shutdownNow) {
                // Give up awaiting termination.
                executorService.shutdownNow();
            }
            e.printStackTrace();
        }
    }

    public ExecutorServiceCleaner() {

        this(new CopyOnWriteArrayList<ExecutorService>());
    }

    public ExecutorServiceCleaner(List<ExecutorService> threadPoolList) {
        Assert.notNull(threadPoolList, "Parameter \"threadPoolList\" must not null. ");
        this.threadPoolList = threadPoolList;
    }

    public Long getAwaitTerminationTime() {

        return awaitTerminationTime;
    }

    public void setAwaitTerminationTime(Long awaitTerminationTime) {

        this.awaitTerminationTime = awaitTerminationTime;
    }

    public Boolean getIgnoreException() {

        return ignoreException;
    }

    public void setIgnoreException(Boolean ignoreException) {
        Assert.notNull(ignoreException, "Parameter \"ignoreException\" must not null. ");
        this.ignoreException = ignoreException;
    }

    public Boolean getShutdownNow() {

        return shutdownNow;
    }

    public void setShutdownNow(Boolean shutdownNow) {
        Assert.notNull(shutdownNow, "Parameter \"shutdownNow\" must not null. ");
        this.shutdownNow = shutdownNow;
    }

    public void add(ExecutorService threadPool) {
        Assert.notNull(threadPool, "Parameter \"threadPool\" must not null. ");
        threadPoolList.add(threadPool);
    }

    public void remove(ExecutorService threadPool) {
        Assert.notNull(threadPool, "Parameter \"threadPool\" must not null. ");
        threadPoolList.remove(threadPool);
    }

    @Override
    public void run() {
        if (CollectionUtils.isEmpty(threadPoolList)) {
            return;
        }
        for (ExecutorService executorService : threadPoolList) {
            if (executorService == null) { continue; }
            try {
                if (shutdownNow) {
                    executorService.shutdownNow();
                }
                else {
                    executorService.shutdown();
                }
                awaitTermination(executorService);
            }
            catch (Exception e) {
                if (ignoreException) {
                    // The logger may not be available when shutdown.
                    e.printStackTrace();
                }
                else {
                    throw ExceptionUtils.wrap(e);
                }
            }
        }
        System.out.println("All thread pools are trying to close. ");
    }

}
