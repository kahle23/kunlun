package com.apyhs.artoria.beans;

import com.apyhs.artoria.entity.Person;
import com.apyhs.artoria.entity.Student;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BeanUtilsTest {
    private static final Person PERSON = new Person();
    private static final Map<String, Object> PERSON_MAP = new HashMap<String, Object>();

    static {
        PERSON.setName("zhangsan");
        PERSON.setAge(21);
        PERSON.setHeight(175D);

        PERSON_MAP.put("name", "lisi");
        PERSON_MAP.put("age", 19);
        PERSON_MAP.put("height", 170D);
    }

    @Test
    public void copyBeanToMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtils.copy(PERSON, map);
        System.out.println(map);
    }

    @Test
    public void copyMapToBean() {
        Student student = new Student();
        BeanUtils.copy(PERSON_MAP, student);
        System.out.println(student);
    }

    @Test
    public void copyBeanToBean() {
        Student student = new Student();
        BeanUtils.copy(PERSON, student);
        System.out.println(student);
    }

}
