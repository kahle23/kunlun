package com.apyhs.artoria.reflect;

import com.apyhs.artoria.entity.Student;
import com.apyhs.artoria.exception.ReflectionException;
import com.apyhs.artoria.reflect.ReflectUtils;
import com.apyhs.artoria.util.DateUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectUtilsTest {

    @Test
    public void newInstance() {
    }

    @Test
    public void newInstance1() {
    }

    @Test
    public void findField() {
    }

    @Test
    public void findFields() throws IllegalAccessException {
        Student student = new Student();
        student.setAge(19);
        student.setName("zhangsan");
        student.setScore(79);
        student.setEmail("zhangsan@email.com");
        Map<String, Field> fields = ReflectUtils.findFields(Student.class);
        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue().get(student));
        }
    }

    @Test
    public void findConstructor() {
    }

    @Test
    public void findMethod() {
    }

    @Test
    public void findSimilarMethod() {
    }

    @Test
    public void findAllGetterAndSetter() {
        Map<String, Method> methods = ReflectUtils.findAllGetterAndSetter(Student.class);
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue().getName());
        }
    }

    @Test
    public void findTypes() {
    }

    @Test
    public void accessible() {
    }

    @Test
    public void create() {
        System.out.println(ReflectUtils.create(Student.class));
    }

    @Test
    public void create1() {
    }

    @Test
    public void create2() {
    }

    @Test
    public void create3() {
    }

    @Test
    public void getClazz() {
    }

    @Test
    public void setClazz() {
    }

    @Test
    public void getBean() {
    }

    @Test
    public void setBean() {
    }

    @Test
    public void newInstance2() {
    }

    @Test
    public void newInstance3() {
    }

    @Test
    public void get() {
    }

    @Test
    public void set() {
    }

    @Test
    public void callGetter() throws ReflectionException {
        ReflectUtils ref = ReflectUtils.create(Student.class).newInstance();
        ref.callSetter("name", "zhangsan");
        ref.callSetter("age", 18);
        ref.callSetter("email", "zhangsan@email.com");
        System.out.println(ref.callGetter("name"));
        System.out.println(ref.callGetter("age"));
        System.out.println(ref.callGetter("email"));
        System.out.println(ref.getBean());
    }

    @Test
    public void call() {
    }

    @Test
    public void call1() {
    }

    @Test
    public void toStringTest() {
        System.out.println(ReflectUtils.create(DateUtils.class));
        System.out.println(ReflectUtils.create(ReflectUtils.class));
    }

}