/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.bean;

import com.alibaba.fastjson.JSON;
import kunlun.data.bean.support.SimpleBeanMap;
import kunlun.data.mock.MockUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

public class BeanMapTest {
    private static Logger log = LoggerFactory.getLogger(BeanMapTest.class);
    private User user = MockUtils.mock(User.class);

    @Test
    public void test1() {
        BeanMap map = new SimpleBeanMap();
        map.setBean(user);
        log.info(JSON.toJSONString(user));
        log.info("{}", map);
        log.info("{}", map.put("name", "lisi"));
        log.info("{}", map);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        BeanMap map = new SimpleBeanMap(user);
        log.info("{}", map);

        BeanMap newMap = (SimpleBeanMap) map.clone();
        log.info("{}", user == newMap.getBean());
        newMap.put("name", "lisi");
        log.info("{}", newMap);
    }

}
