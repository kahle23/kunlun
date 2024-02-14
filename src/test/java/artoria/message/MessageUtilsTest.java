package artoria.message;

import artoria.common.Constants;
import artoria.data.Dict;
import artoria.data.json.JsonUtils;
import artoria.data.json.support.AbstractJsonHandler;
import artoria.message.handler.ConsoleHandler;
import artoria.message.handler.LogHandler;
import artoria.mock.MockUtils;
import artoria.test.pojo.entity.system.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.lang.reflect.Type;

public class MessageUtilsTest {
    private static final String CONSOLE = "console";
    private static final String LOG = "log";

    static {
        JsonUtils.registerHandler(Constants.DEFAULT, new AbstractJsonHandler() {
            @Override
            public String toJsonString(Object object, Object... arguments) {

                return JSON.toJSONString(object);
            }
            @Override
            public <T> T parseObject(String jsonString, Type type, Object... arguments) {

                return JSON.parseObject(jsonString, type);
            }
        });
        Dict dict = Dict.of("hostname", Constants.HOST_NAME);
        MessageUtils.getMessageProvider().registerCommonProperties(dict);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        LogHandler logHandler = new LogHandler();
        MessageUtils.registerHandler(CONSOLE, consoleHandler);
        MessageUtils.registerHandler(LOG, logHandler);
    }

    @Test
    public void test1() {
        MessageUtils.send("Hello, World! console", CONSOLE, Boolean.class);
        MessageUtils.send("Hello, World! log", LOG, Boolean.class);
    }

    @Test
    public void test2() {
        User user = MockUtils.mock(User.class);
        MessageUtils.send(user, LOG, Boolean.class);
    }

}
