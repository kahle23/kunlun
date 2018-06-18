package artoria.beans;

import com.alibaba.fastjson.JSON;
import artoria.entity.Person;
import artoria.entity.Student;
import artoria.util.RandomUtils;
import org.junit.Test;

import java.util.*;

public class BeanUtilsTest {
    private static Person person = RandomUtils.nextObject(Person.class);
    private static Map<String, Object> personMap = BeanUtils.beanToMap(RandomUtils.nextObject(Person.class));

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
        BeanUtils.setBeanCopier(new JdkBeanCopier());
        List<String> ignore = new ArrayList<String>();
        Collections.addAll(ignore, "name", "age", "123test");
        Student student = new Student();
        BeanUtils.copy(person, student, ignore);
        System.out.println(JSON.toJSONString(student));
    }

}
