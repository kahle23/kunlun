package artoria.util;

import artoria.beans.Student;
import artoria.exception.ReflectionException;
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
    public void copyProperties2() {
    }

    @Test
    public void findAllGetterOrSetterMethods() {
        Map<String, Method> methods = BeanUtils.findAllGetOrSetMethods(Student.class);
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue().getName());
        }
    }
}