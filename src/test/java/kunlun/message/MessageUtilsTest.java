/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message;

import com.alibaba.fastjson.JSON;
import kunlun.common.constant.Env;
import kunlun.common.constant.Words;
import kunlun.data.Dict;
import kunlun.data.json.JsonUtils;
import kunlun.data.json.support.AbstractJsonHandler;
import kunlun.data.mock.MockUtils;
import kunlun.message.support.ConsoleHandler;
import kunlun.message.support.LogHandler;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

import java.lang.reflect.Type;

public class MessageUtilsTest {
    private static final String CONSOLE = "console";
    private static final String LOG = "log";

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
        Dict dict = Dict.of("hostname", Env.HOST_NAME);
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
