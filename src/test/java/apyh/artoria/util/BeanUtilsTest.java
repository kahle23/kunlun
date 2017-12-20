package apyh.artoria.util;

import apyh.artoria.beans.Student;
import apyh.artoria.exception.ReflectionException;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanUtilsTest {

    @Test
    public void ifNull() {
    }

    @Test
    public void isNull() {
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void isArray() {
    }

    @Test
    public void cloneTest() {
    }

    @Test
    public void deepClone() {
    }

    @Test
    public void copyProperties() {
    }

    @Test
    public void copyProperties1() throws ReflectionException {
        Student student = new Student();
        student.setName("zhangsan");
        student.setAge(19);
        student.setHeight(176d);
        student.setScore(89);
        student.setEmail("zhangsan@email.com");
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtils.copyProperties(student, map);
        System.out.println(map);

        Student student1 = new Student();
        BeanUtils.copyProperties(map, student1);
        System.out.println(student1);
    }

    @Test
    public void copyProperties2() throws ReflectionException {
        Student student = new Student();
        student.setName("zhangsan");
        student.setAge(19);
        student.setHeight(176d);
        student.setScore(89);
        student.setEmail("zhangsan@email.com");
        System.out.println(student);

        Student student1 = new Student();
        BeanUtils.copyProperties(student, student1);
        System.out.println(student1);

        student1.setAge(23);
        System.out.println(student);
        System.out.println(student1);
    }

    @Test
    public void findAllGetterOrSetterMethods() {
        Map<String, Method> methods = BeanUtils.findAllGetOrSetMethods(Student.class);
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue().getName());
        }
    }
}