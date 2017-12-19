package artoria.util;

import artoria.beans.Student;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.Assert.*;

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
    public void copyProperties1() {
    }

    @Test
    public void copyProperties2() {
    }

    @Test
    public void findAllGetterOrSetterMethods() {
        Map<String, Method> methods = BeanUtils.findAllGetterOrSetterMethods(Student.class);
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue().getName());
        }
    }
}