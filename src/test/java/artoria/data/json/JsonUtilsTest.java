package artoria.data.json;

import artoria.common.Constants;
import artoria.data.json.support.AbstractJsonHandler;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.mock.MockUtils;
import artoria.test.pojo.entity.system.User;
import artoria.util.TypeUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.FIVE;
import static artoria.common.Constants.ZERO;

public class JsonUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(JsonUtilsTest.class);
    private final Map<Long, User> data2 = new HashMap<Long, User>();
    private final List<User> data1 = new ArrayList<User>();
    private User data = new User();
    private String jsonString = null;
    private String jsonString1 = null;
    private String jsonString2 = null;

    @Before
    public void init() {
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
        data = MockUtils.mock(User.class);
        for (int i = ZERO; i < FIVE; i++) {
            User user = MockUtils.mock(User.class);
            data1.add(user);
            data2.put(user.getId(), user);
        }
        jsonString = JsonUtils.toJsonString(data);
        jsonString1 = JsonUtils.toJsonString(data1);
        jsonString2 = JsonUtils.toJsonString(data2);
    }

    @Test
    public void test0() {
        log.info(JsonUtils.toJsonString(data));
        log.info(JsonUtils.toJsonString(data1));
        log.info(JsonUtils.toJsonString(data2));
    }

    @Test
    public void test1() {
        User user = JsonUtils.parseObject(jsonString, User.class);
        log.info(JsonUtils.toJsonString(user));
    }

    @Test
    public void test2() {
        List<User> list = JsonUtils.parseObject(jsonString1
                , TypeUtils.parameterizedOf(List.class, User.class));
        log.info(JsonUtils.toJsonString(list));
    }

    @Test
    public void test3() {
        Map<Long, User> map = JsonUtils.parseObject(jsonString2
                , TypeUtils.parameterizedOf(Map.class, Long.class, User.class));
        log.info(JsonUtils.toJsonString(map));
    }

}
