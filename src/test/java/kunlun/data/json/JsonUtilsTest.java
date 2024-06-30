/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.json;

import com.alibaba.fastjson.JSON;
import kunlun.common.constant.Words;
import kunlun.data.json.support.AbstractJsonHandler;
import kunlun.data.mock.MockUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.system.User;
import kunlun.util.TypeUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kunlun.common.constant.Numbers.FIVE;
import static kunlun.common.constant.Numbers.ZERO;

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
