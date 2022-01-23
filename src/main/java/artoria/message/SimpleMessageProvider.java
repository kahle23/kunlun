package artoria.message;

import artoria.lang.Code;
import artoria.lang.callback.FailureCallback;
import artoria.lang.callback.SuccessCallback;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.thread.SimpleThreadFactory;
import artoria.util.Assert;
import artoria.util.ObjectUtils;
import artoria.util.ShutdownHookUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static artoria.common.Constants.SIXTY;
import static artoria.common.Constants.ZERO;
import static java.lang.Boolean.FALSE;
import static java.lang.Integer.MAX_VALUE;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The simple message provider.
 * @author Kahle
 */
public class SimpleMessageProvider extends ClassBasedMessageProvider implements AsyncMessageProvider {
    private static final String ASYNC_SEND_THREAD_NAME = "message-async-send-executor";
    private static Logger log = LoggerFactory.getLogger(SimpleMessageProvider.class);
    private final ExecutorService executorService;

    public SimpleMessageProvider() {
        this(new ThreadPoolExecutor(ZERO, MAX_VALUE, SIXTY, SECONDS,
                new SynchronousQueue<Runnable>(),
                new SimpleThreadFactory(ASYNC_SEND_THREAD_NAME, FALSE)
        ));
    }

    public SimpleMessageProvider(ExecutorService executorService) {
        Assert.notNull(executorService, "Parameter \"executorService\" must not null. ");
        ShutdownHookUtils.addExecutorService(executorService);
        this.executorService = executorService;
    }

    protected ExecutorService getExecutorService() {

        return executorService;
    }

    @Override
    public void sendAsync(Object message, SuccessCallback<?> fine, FailureCallback fail, Code<?>... types) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        if (message instanceof List) { batchSend((List<?>) message, types); return; }
        Runnable task = new AsyncSendTask(this, message, types, fine, fail);
        executorService.execute(task);
    }

    @Override
    public void batchSendAsync(List<?> messages, SuccessCallback<?> fine, FailureCallback fail, Code<?>... types) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not null. ");
        Runnable task = new AsyncBatchSendTask(this, messages, types, fine, fail);
        executorService.execute(task);
    }

    /**
     * The abstract send task.
     */
    protected static abstract class AbstractSendTask implements Runnable {
        private final MessageProvider messageProvider;
        private SuccessCallback<Object> success;
        private FailureCallback failure;

        protected AbstractSendTask(MessageProvider messageProvider,
                                   SuccessCallback<?> success,
                                   FailureCallback failure) {
            Assert.notNull(messageProvider, "Parameter \"messageProvider\" must not null. ");
            Assert.notNull(success, "Parameter \"success\" must not null. ");
            Assert.notNull(failure, "Parameter \"failure\" must not null. ");
            this.messageProvider = messageProvider;
            this.success = ObjectUtils.cast(success);
            this.failure = failure;
        }

        protected MessageProvider getMessageProvider() {

            return messageProvider;
        }

        protected void onSuccess(Object result) {
            try {
                success.onSuccess(result);
            }
            catch (Exception ex) {
                log.error("Success callback error. ", ex);
            }
        }

        protected void onFailure(Throwable th) {
            try {
                failure.onFailure(th);
            }
            catch (Exception exc) {
                log.error("Failure callback error. ", exc);
            }
        }
    }

    /**
     * The asynchronous send task.
     */
    protected static class AsyncSendTask extends AbstractSendTask {
        private Code<?>[] types;
        private Object message;

        protected AsyncSendTask(MessageProvider messageProvider,
                                Object message,
                                Code<?>[] types,
                                SuccessCallback<?> success,
                                FailureCallback failure) {
            super(messageProvider, success, failure);
            Assert.notNull(message, "Parameter \"message\" must not null. ");
            this.message = message;
            this.types = types;
        }

        @Override
        public void run() {
            try {
                Object send = getMessageProvider().send(message, types);
                onSuccess(send);
            }
            catch (Exception ex) {
                log.error("Send message error. ", ex);
                onFailure(ex);
            }
        }
    }

    /**
     * The asynchronous batch send task.
     */
    protected static class AsyncBatchSendTask extends AbstractSendTask {
        private Code<?>[] types;
        private List<?> messages;

        protected AsyncBatchSendTask(MessageProvider messageProvider,
                                     List<?> messages,
                                     Code<?>[] types,
                                     SuccessCallback<?> success,
                                     FailureCallback failure) {
            super(messageProvider, success, failure);
            Assert.notEmpty(messages, "Parameter \"messages\" must not null. ");
            this.messages = messages;
            this.types = types;
        }

        @Override
        public void run() {
            try {
                Object batchSend = getMessageProvider().batchSend(messages, types);
                onSuccess(batchSend);
            }
            catch (Exception ex) {
                log.error("Batch send message error. ", ex);
                onFailure(ex);
            }
        }
    }

}
