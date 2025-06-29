package kunlun.message;

import com.alibaba.fastjson.JSON;
import kunlun.message.support.SimpleMessageBus;
import kunlun.common.constant.Words;
import kunlun.data.json.JsonUtil;
import kunlun.data.json.support.AbstractJsonHandler;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.message.model.Message;
import kunlun.message.model.MessageRt;
import kunlun.message.model.Subscribe;
import org.junit.Test;

import java.lang.reflect.Type;

import static java.util.Collections.singletonList;

public class MessageBusTest {
    private static final Logger log = LoggerFactory.getLogger(MessageBusTest.class);
    private static final MessageBus bus = new SimpleMessageBus();

    static {
        JsonUtil.registerHandler(Words.DEFAULT, new AbstractJsonHandler() {
            @Override
            public String toJsonString(Object object, Object... arguments) {

                return JSON.toJSONString(object);
            }
            @Override
            public <T> T parseObject(String jsonString, Type type, Object... arguments) {

                return JSON.parseObject(jsonString, type);
            }
        });
        bus.subscribe(new Subscribe("test", new MessageListener() {
            @Override
            public Object onMessage(Message message) {
                log.info(JsonUtil.toJsonString(message));
                return null;
            }
        }));
    }

    @Test
    public void test1() {
        MessageRt result = bus.send(singletonList(new Message("test", "data")));
        log.info(JsonUtil.toJsonString(result));
    }

}
