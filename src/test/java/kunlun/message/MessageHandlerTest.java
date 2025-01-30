package kunlun.message;

import com.alibaba.fastjson.JSON;
import kunlun.action.message.support.SimpleMessageHandler;
import kunlun.common.constant.Words;
import kunlun.data.json.JsonUtils;
import kunlun.data.json.support.AbstractJsonHandler;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.message.model.Message;
import kunlun.message.model.Result;
import org.junit.Test;

import java.lang.reflect.Type;

import static java.util.Collections.singletonList;

public class MessageHandlerTest {
    private static final Logger log = LoggerFactory.getLogger(MessageHandlerTest.class);
    private final MessageHandler messageHandler = new SimpleMessageHandler();

    static {
        JsonUtils.registerHandler(Words.DEFAULT, new AbstractJsonHandler() {
            @Override
            public String toJsonString(Object object, Object... arguments) {

                return JSON.toJSONString(object);
            }
            @Override
            public <T> T parseObject(String jsonString, Type type, Object... arguments) {

                return JSON.parseObject(jsonString, type);
            }
        });
    }

    @Test
    public void test1() {
        Result result = messageHandler.send(
                singletonList(new Message("test", "data")));
        log.info(JsonUtils.toJsonString(result));
    }

}
