package artoria.message;

import artoria.common.Constants;
import artoria.exchange.JsonFeature;
import artoria.exchange.JsonUtils;
import artoria.exchange.SimpleJsonProvider;
import artoria.lang.Dict;
import artoria.lang.callback.FailureCallback;
import artoria.lang.callback.SuccessCallback;
import artoria.mock.MockUtils;
import artoria.test.bean.User;
import artoria.util.ThreadUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.lang.reflect.Type;

import static artoria.message.MessageType.CONSOLE;
import static artoria.message.MessageType.LOG;

public class MessageUtilsTest {

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
        AbstractMessageProvider messageProvider = (AbstractMessageProvider) MessageUtils.getMessageProvider();
        messageProvider.registerCommonProperties(Dict.of("hostname", Constants.HOST_NAME));
    }

    @Test
    public void test1() {
        MessageUtils.send("Hello, World! ", CONSOLE, LOG);
        MessageUtils.sendAsync("Async: Hello, World! ", new SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object result) {}
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable th) {}
        }, LOG);
    }

    @Test
    public void test2() {
        User user = MockUtils.mock(User.class);
        MessageUtils.send(user, CONSOLE, LOG);
    }

    @Test
    public void test3() {
        for (int i = 0; i < 2000; i++) {
            MessageUtils.sendAsync("Hello, World! ", new SuccessCallback<Object>() {
                @Override
                public void onSuccess(Object result) {}
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable th) {}
            }, LOG);
        }
        ThreadUtils.sleepQuietly(1000L);
    }

}
