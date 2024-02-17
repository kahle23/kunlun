/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.handler;

import kunlun.core.Handler;
import kunlun.core.callback.FailureCallback;
import kunlun.core.callback.SuccessCallback;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * The asynchronous supported handler.
 *
 * The main flow:
 * buildContext -> async or sync
 *                 async -> submit task (include doExecute) -> getResult
 *                 sync  -> doExecute (include getResult)   -> or getResult
 *
 * @author Kahle
 */
public interface AsyncSupportHandler extends ContextSupportHandler, Handler {

    /**
     * Build the asynchronous supported context from the arguments array.
     * @param arguments The specific arguments array
     * @return The asynchronous supported context
     */
    @Override
    AsyncSupportContext buildContext(Object... arguments);

    /**
     * Get the result to return, which varies depending on the "finish" field.
     * @param context The asynchronous support context
     * @return The result to return
     */
    Object getResult(AsyncSupportContext context);

    /**
     * The logic to be executed (synchronous or asynchronous).
     * @param context The asynchronous support context
     * @return The result of execute
     */
    Object doExecute(AsyncSupportContext context);

    /**
     * The asynchronous supported context.
     * @author Kahle
     */
    class AsyncSupportContext implements HandlerContext {
        private Object[] arguments;
        private SuccessCallback<Object> successCallback;
        private FailureCallback failureCallback;
        private ExecutorService threadPool;
        private Future<?> future;
        private Boolean finish;
        private Boolean async;

        @Override
        public Object[] getArguments() {

            return arguments;
        }

        public void setArguments(Object[] arguments) {

            this.arguments = arguments;
        }

        public Boolean getAsync() {

            return async;
        }

        public void setAsync(Boolean async) {

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

        public FailureCallback getFailureCallback() {

            return failureCallback;
        }

        public void setFailureCallback(FailureCallback failureCallback) {

            this.failureCallback = failureCallback;
        }

        public SuccessCallback<Object> getSuccessCallback() {

            return successCallback;
        }

        public void setSuccessCallback(SuccessCallback<Object> successCallback) {

            this.successCallback = successCallback;
        }

    }

    /**
     * The asynchronous execute task.
     * @author Kahle
     */
    class AsyncExecuteTask implements Callable<Object> {
        private static final Logger log = LoggerFactory.getLogger(AsyncExecuteTask.class);
        private final AsyncSupportHandler asyncSupportHandler;
        private final AsyncSupportContext context;

        public AsyncExecuteTask(AsyncSupportHandler asyncSupportHandler,
                                AsyncSupportContext context) {
            Assert.notNull(asyncSupportHandler, "Parameter \"asyncSupportHandler\" must not null. ");
            Assert.notNull(context, "Parameter \"context\" must not null. ");
            this.asyncSupportHandler = asyncSupportHandler;
            this.context = context;
        }

        @Override
        public Object call() throws Exception {
            SuccessCallback<Object> successCallback = context.getSuccessCallback();
            FailureCallback failureCallback = context.getFailureCallback();
            try {
                Object execute = asyncSupportHandler.doExecute(context);
                if (successCallback != null) {
                    successCallback.onSuccess(execute);
                }
                return execute;
            }
            catch (Exception e) {
                log.error("The asynchronous execute error! ", e);
                if (failureCallback != null) {
                    failureCallback.onFailure(e);
                }
                return null;
            }
        }

    }

}
