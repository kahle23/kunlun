package artoria.message;

import artoria.common.Constants;
import artoria.data.json.JsonFeature;
import artoria.data.json.JsonUtils;
import artoria.data.json.SimpleJsonProvider;
import artoria.lang.Dict;
import artoria.message.handler.ConsoleHandler;
import artoria.message.handler.LogHandler;
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
        ConsoleHandler consoleHandler = new ConsoleHandler();
        LogHandler logHandler = new LogHandler();
        MessageUtils.registerHandler(CONSOLE, consoleHandler);
        MessageUtils.registerHandler(LOG, logHandler);
        MessageUtils.registerHandler("class:" + User.class.getName(), consoleHandler);
    }

    @Test
    public void test1() {
        MessageUtils.send("Hello, World! console", CONSOLE, Boolean.class);
        MessageUtils.send("Hello, World! log", LOG, Boolean.class);
    }

    @Test
    public void test2() {
        User user = MockUtils.mock(User.class);
        MessageUtils.send(user, Boolean.class);
        MessageUtils.send(user, LOG, Boolean.class);
    }

}
