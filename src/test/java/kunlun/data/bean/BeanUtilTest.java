/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.bean;

import com.alibaba.fastjson.JSON;
import kunlun.data.mock.MockUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtilTest {
    private static Logger log = LoggerFactory.getLogger(BeanUtilTest.class);
    private static User person = MockUtil.mock(User.class);
    private static Map<String, Object> personMap = new HashMap<String, Object>();
    private static List<User> persons = new ArrayList<User>();
    private static List<Map<String, Object>> personsMap;

    static {
        BeanUtil.copy(person, personMap);
        persons.add(MockUtil.mock(User.class));
        personsMap = BeanUtil.beanToMapInList(persons);
    }

    @Test
    public void testCopyBeanToMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.copy(person, map);
        log.info("{}", map);
    }

    @Test
    public void testCopyMapToBean() {
        User user = new User();
        BeanUtil.copy(personMap, user);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void testCopyBeanToBean() {
        User user = new User();
        BeanUtil.copy(person, user);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void mapToBean() throws Exception {
        User user = BeanUtil.mapToBean(personMap, User.class);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void mapToBean1() throws Exception {
        User user = new User();
        BeanUtil.mapToBean(personMap, user);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void beanToMap() throws Exception {
        Map<String, Object> map = BeanUtil.beanToMap(person);
        log.info(JSON.toJSONString(map));
    }

    @Test
    public void beanToBean() throws Exception {
        User user = BeanUtil.beanToBean(person, User.class);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void beanToBean1() throws Exception {
        User user = new User();
        BeanUtil.beanToBean(person, user);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void mapToBeanInList() throws Exception {
        log.info("{}", personsMap);
        List<User> users = BeanUtil.mapToBeanInList(personsMap, User.class);
        log.info(JSON.toJSONString(users));
    }

    @Test
    public void beanToMapInList() throws Exception {
        List<Map<String, Object>> list = BeanUtil.beanToMapInList(persons);
        log.info(JSON.toJSONString(list));
    }

    @Test
    public void beanToBeanInList() throws Exception {
        List<User> users = BeanUtil.beanToBeanInList(persons, User.class);
        log.info(JSON.toJSONString(users));
    }

}
