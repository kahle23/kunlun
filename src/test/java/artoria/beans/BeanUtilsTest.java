package artoria.beans;

import artoria.entity.Person;
import artoria.entity.Student;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.random.RandomUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtilsTest {
    private static Logger log = LoggerFactory.getLogger(BeanUtilsTest.class);
    private static Person person = RandomUtils.nextObject(Student.class);
    private static Map<String, Object> personMap = new HashMap<String, Object>();
    private static List<Person> persons = new ArrayList<Person>();
    private static List<Map<String, Object>> personsMap;

    static {
        BeanUtils.copy(person, personMap);
        persons.add(RandomUtils.nextObject(Student.class));
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
        Student student = new Student();
        BeanUtils.copy(personMap, student);
        log.info(JSON.toJSONString(student));
    }

    @Test
    public void testCopyBeanToBean() {
        Student student = new Student();
        BeanUtils.copy(person, student);
        log.info(JSON.toJSONString(student));
    }

    @Test
    public void mapToBean() throws Exception {
        Student student = BeanUtils.mapToBean(personMap, Student.class);
        log.info(JSON.toJSONString(student));
    }

    @Test
    public void mapToBean1() throws Exception {
        Student student = new Student();
        BeanUtils.mapToBean(personMap, student);
        log.info(JSON.toJSONString(student));
    }

    @Test
    public void beanToMap() throws Exception {
        Map<String, Object> map = BeanUtils.beanToMap(person);
        log.info(JSON.toJSONString(map));
    }

    @Test
    public void beanToBean() throws Exception {
        Student student = BeanUtils.beanToBean(person, Student.class);
        log.info(JSON.toJSONString(student));
    }

    @Test
    public void beanToBean1() throws Exception {
        Student student = new Student();
        BeanUtils.beanToBean(person, student);
        log.info(JSON.toJSONString(student));
    }

    @Test
    public void mapToBeanInList() throws Exception {
        log.info("{}", personsMap);
        List<Student> students = BeanUtils.mapToBeanInList(personsMap, Student.class);
        log.info(JSON.toJSONString(students));
    }

    @Test
    public void beanToMapInList() throws Exception {
        List<Map<String, Object>> list = BeanUtils.beanToMapInList(persons);
        log.info(JSON.toJSONString(list));
    }

    @Test
    public void beanToBeanInList() throws Exception {
        List<Student> students = BeanUtils.beanToBeanInList(persons, Student.class);
        log.info(JSON.toJSONString(students));
    }

}
