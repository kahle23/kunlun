/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import com.alibaba.fastjson.JSON;
import kunlun.data.mock.MockUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.system.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecombineUtilTest {
    private static Logger log = LoggerFactory.getLogger(RecombineUtilTest.class);
    private List<User> list = new ArrayList<User>();

    @Before
    public void init() {
        list.add(null);
        list.add(MockUtils.mock(User.class));
        list.add(MockUtils.mock(User.class));
        list.add(MockUtils.mock(User.class));
        list.add(null);
        list.add(MockUtils.mock(User.class));
        list.add(MockUtils.mock(User.class));
    }

    @Test
    public void testListToListList() {
        List<List<User>> lists = RecombineUtil.listToListList(list, 2);
        for (List<User> people : lists) {
            log.info(JSON.toJSONString(people));
        }
    }

    @Test
    public void testListToListProperty() {
        List<String> list = RecombineUtil.listToListProperty(this.list, "name", String.class);
        log.info(JSON.toJSONString(list, true));
    }

    @Test
    public void testListToMapBean() {
        Map<String, User> map = RecombineUtil.listToMapBean(list, "name");
        log.info(JSON.toJSONString(map, true));
    }

    @Test
    public void testListToMapList() {
        Map<String, List<User>> map = RecombineUtil.listToMapList(list, "name");
        log.info(JSON.toJSONString(map, true));
    }

    @Test
    public void testListToMapProperty() {
        Map<String, Object> map = RecombineUtil.listToMapProperty(list, "age", "name");
        log.info(JSON.toJSONString(map, true));
    }

}
