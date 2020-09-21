package artoria.beans;

import artoria.fake.FakeUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.test.bean.Person;
import artoria.test.bean.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtilsTest {
    private static Logger log = LoggerFactory.getLogger(BeanUtilsTest.class);
    private static Person person = FakeUtils.fake(User.class);
    private static Map<String, Object> personMap = new HashMap<String, Object>();
    private static List<Person> persons = new ArrayList<Person>();
    private static List<Map<String, Object>> personsMap;

    static {
        BeanUtils.copy(person, personMap);
        persons.add(FakeUtils.fake(User.class));
        personsMap = BeanUtils.beanToMapInList(persons);
    }

    @Test
    public void testCopyBeanToMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtils.copy(person, map);
        log.info("{}", map);
    }

    @Test
    public void testCopyMapToBean() {
        User user = new User();
        BeanUtils.copy(personMap, user);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void testCopyBeanToBean() {
        User user = new User();
        BeanUtils.copy(person, user);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void mapToBean() throws Exception {
        User user = BeanUtils.mapToBean(personMap, User.class);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void mapToBean1() throws Exception {
        User user = new User();
        BeanUtils.mapToBean(personMap, user);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void beanToMap() throws Exception {
        Map<String, Object> map = BeanUtils.beanToMap(person);
        log.info(JSON.toJSONString(map));
    }

    @Test
    public void beanToBean() throws Exception {
        User user = BeanUtils.beanToBean(person, User.class);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void beanToBean1() throws Exception {
        User user = new User();
        BeanUtils.beanToBean(person, user);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void mapToBeanInList() throws Exception {
        log.info("{}", personsMap);
        List<User> users = BeanUtils.mapToBeanInList(personsMap, User.class);
        log.info(JSON.toJSONString(users));
    }

    @Test
    public void beanToMapInList() throws Exception {
        List<Map<String, Object>> list = BeanUtils.beanToMapInList(persons);
        log.info(JSON.toJSONString(list));
    }

    @Test
    public void beanToBeanInList() throws Exception {
        List<User> users = BeanUtils.beanToBeanInList(persons, User.class);
        log.info(JSON.toJSONString(users));
    }

}
