package artoria.message;

import artoria.common.Constants;
import artoria.exchange.JsonFeature;
import artoria.exchange.JsonUtils;
import artoria.exchange.SimpleJsonProvider;
import artoria.lang.Dict;
import artoria.message.sender.ConsoleSender;
import artoria.message.sender.LogSender;
import artoria.mock.MockUtils;
import artoria.test.bean.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.lang.reflect.Type;

public class MessageUtilsTest {
    private static String CONSOLE = "console";
    private static String LOG = "log";

    static {
        JsonUtils.setJsonProvider(new SimpleJsonProvider() {
            @Override
            public String toJsonString(Object object, JsonFeature... features) {
                return JSON.toJSONString(object);
            }
            @Override
            public <T> T parseObject(String jsonString, Type type, JsonFeature... features) {
                return JSON.parseObject(jsonString, type);
            }
        });
        Dict dict = Dict.of("hostname", Constants.HOST_NAME);
        MessageUtils.getMessageProvider().registerCommonProperties(dict);
        MessageUtils.registerSender(User.class, CONSOLE, new ConsoleSender());
        MessageUtils.registerSender(User.class, LOG, new LogSender());
    }

    @Test
    public void test1() {
        MessageUtils.send("Hello, World! console", CONSOLE, Boolean.class);
        MessageUtils.send("Hello, World! log", LOG, Boolean.class);
    }

    @Test
    public void test2() {
        User user = MockUtils.mock(User.class);
        MessageUtils.send(user, CONSOLE, Boolean.class);
        MessageUtils.send(user, LOG, Boolean.class);
    }

}
