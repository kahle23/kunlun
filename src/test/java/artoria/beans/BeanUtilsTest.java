package artoria.beans;

import artoria.entity.Person;
import artoria.entity.Student;
import artoria.util.RandomUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.*;

public class BeanUtilsTest {
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
        System.out.println(map);
    }

    @Test
    public void testCopyMapToBean() {
        Student student = new Student();
        BeanUtils.copy(personMap, student);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void testCopyBeanToBean() {
        Student student = new Student();
        BeanUtils.copy(person, student);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void testIgnoreJdkCopy() {
        BeanUtils.setBeanCopier(new SimpleBeanCopier());
        List<String> ignore = new ArrayList<String>();
        Collections.addAll(ignore, "name", "age", "123test");
        Student student = new Student();
        BeanUtils.copy(person, student, ignore);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void mapToBean() throws Exception {
        Student student = BeanUtils.mapToBean(personMap, Student.class);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void mapToBean1() throws Exception {
        Student student = new Student();
        BeanUtils.mapToBean(personMap, student);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void beanToMap() throws Exception {
        Map<String, Object> map = BeanUtils.beanToMap(person);
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void beanToBean() throws Exception {
        Student student = BeanUtils.beanToBean(person, Student.class);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void beanToBean1() throws Exception {
        Student student = new Student();
        BeanUtils.beanToBean(person, student);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void mapToBeanInList() throws Exception {
        System.out.println(personsMap);
        List<Student> students = BeanUtils.mapToBeanInList(personsMap, Student.class);
        System.out.println(JSON.toJSONString(students));
    }

    @Test
    public void beanToMapInList() throws Exception {
        List<Map<String, Object>> list = BeanUtils.beanToMapInList(persons);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void beanToBeanInList() throws Exception {
        List<Student> students = BeanUtils.beanToBeanInList(persons, Student.class);
        System.out.println(JSON.toJSONString(students));
    }

}
