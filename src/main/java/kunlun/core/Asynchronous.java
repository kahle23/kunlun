/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import kunlun.core.function.Consumer;
import kunlun.core.function.Function;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * The abstraction that provide support for asynchronous.
 * <p>
 * The main flow:
 *      build context -> context.execute(this)
 * @author Kahle
 */
public interface Asynchronous {

    /**
     * The logic to be executed (synchronous or asynchronous).
     * @param context The asynchronous support context
     * @return The result of execute
     */
    Object doAsync(AsyncContext context);

    /**
     * The asynchronous supported context.
     * @author Kahle
     */
    class AsyncContext {

        public Object execute(Asynchronous asynchronous) {
            Assert.notNull(this.getResultExtractor());
            Assert.notNull(asynchronous);
            if (!this.getAsync()) { return asynchronous.doAsync(this); }
            else {
                // Asynchronous call.
                AsyncTask task = new AsyncTask(asynchronous, this);
                this.setFuture(this.getThreadPool().submit(task));
                // Get the result.
                this.setFinish(false);
                return this.getResultExtractor().apply(this);
            }
        }

        // ====

        private Function<AsyncContext, Object> resultExtractor;
        private Consumer<Throwable> failureCallback;
        private Consumer<Object> successCallback;
        private ExecutorService threadPool;
        private Future<?> future;
        private Boolean finish;
        private boolean async;

        public boolean getAsync() {

            return async;
        }

        public void setAsync(boolean async) {

            this.async = async;
        }

        public Boolean getFinish() {

            return finish;
        }

        public void setFinish(Boolean finish) {

            this.finish = finish;
        }

        public Future<?> getFuture() {

            return future;
        }

        public void setFuture(Future<?> future) {

            this.future = future;
        }

        public ExecutorService getThreadPool() {

            return threadPool;
        }

        public void setThreadPool(ExecutorService threadPool) {

            this.threadPool = threadPool;
        }

        public Consumer<Object> getSuccessCallback() {

            return successCallback;
        }

        public void setSuccessCallback(Consumer<Object> successCallback) {

            this.successCallback = successCallback;
        }

        public Consumer<Throwable> getFailureCallback() {

            return failureCallback;
        }

        public void setFailureCallback(Consumer<Throwable> failureCallback) {

            this.failureCallback = failureCallback;
        }

        public Function<AsyncContext, Object> getResultExtractor() {

            return resultExtractor;
        }

        public void setResultExtractor(Function<AsyncContext, Object> resultExtractor) {

            this.resultExtractor = resultExtractor;
        }
    }

    /**
     * The asynchronous execute task.
     * @author Kahle
     */
    class AsyncTask implements Callable<Object> {
        private static final Logger log = LoggerFactory.getLogger(AsyncTask.class);
        private final Asynchronous asynchronous;
        private final AsyncContext context;

        public AsyncTask(Asynchronous asynchronous, AsyncContext context) {
            this.asynchronous = Assert.notNull(asynchronous);
            this.context = Assert.notNull(context);
        }

        @Override
        public Object call() throws Exception {
            Consumer<Throwable> failureCallback = context.getFailureCallback();
            Consumer<Object> successCallback = context.getSuccessCallback();
            try {
                Object execute = asynchronous.doAsync(context);
                if (successCallback != null) {
                    successCallback.accept(execute);
                }
                return execute;
            }
            catch (Exception e) {
                log.error("The asynchronous execute error! ", e);
                if (failureCallback != null) {
                    failureCallback.accept(e);
                }
                return null;
            }
        }
    }

}
