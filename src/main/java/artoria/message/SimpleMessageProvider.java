package artoria.message;

import artoria.lang.Code;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.thread.SimpleThreadFactory;
import artoria.util.Assert;
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
 * The simple message send provider.
 * @author Kahle
 */
public class SimpleMessageProvider extends AbstractMessageProvider {
    private static final String ASYNC_SEND_THREAD_NAME = "message-async-send-thread";
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
    public void sendAsync(Object message, Code<?>... types) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
        if (message instanceof List) { batchSend((List<?>) message, types); return; }
        Runnable task = new AsyncSendTask(this, message, types);
        executorService.execute(task);
    }

    @Override
    public void batchSendAsync(List<?> messages, Code<?>... types) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not null. ");
        Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
        Runnable task = new AsyncBatchSendTask(this, messages, types);
        executorService.execute(task);
    }

    protected static class AsyncSendTask implements Runnable {
        private final MessageProvider messageProvider;
        private Code<?>[] types;
        private Object message;

        protected AsyncSendTask(MessageProvider messageProvider, Object message, Code<?>[] types) {
            Assert.notNull(messageProvider, "Parameter \"messageProvider\" must not null. ");
            Assert.notNull(message, "Parameter \"message\" must not null. ");
            Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
            this.messageProvider = messageProvider;
            this.message = message;
            this.types = types;
        }

        protected void failure(Object message, Code<?>[] types, Exception ex) {

        }

        @Override
        public void run() {
            try {
                messageProvider.send(message, types);
            }
            catch (Exception ex) {
                log.error("Send message error. ", ex);
                failure(message, types, ex);
            }
        }
    }

    protected static class AsyncBatchSendTask implements Runnable {
        private final MessageProvider messageProvider;
        private List<?> messages;
        private Code<?>[] types;

        protected AsyncBatchSendTask(MessageProvider messageProvider, List<?> messages, Code<?>[] types) {
            Assert.notNull(messageProvider, "Parameter \"messageProvider\" must not null. ");
            Assert.notEmpty(messages, "Parameter \"messages\" must not null. ");
            Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
            this.messageProvider = messageProvider;
            this.messages = messages;
            this.types = types;
        }

        protected void failure(List<?> messages, Code<?>[] types, Exception ex) {

        }

        @Override
        public void run() {
            try {
                messageProvider.batchSend(messages, types);
            }
            catch (Exception ex) {
                log.error("Batch send message error. ", ex);
                failure(messages, types, ex);
            }
        }
    }

}
