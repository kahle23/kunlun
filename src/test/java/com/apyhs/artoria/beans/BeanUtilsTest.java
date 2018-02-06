package com.apyhs.artoria.beans;

import com.apyhs.artoria.entity.Person;
import com.apyhs.artoria.entity.Student;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BeanUtilsTest {
    private static Person person = new Person();
    private static Map<String, Object> personMap = new HashMap<String, Object>();

    static {
        person.setName("zhangsan");
        person.setAge(21);
        person.setHeight(175d);

        personMap.put("name", "lisi");
        personMap.put("age", 19);
        personMap.put("height", 170d);
    }

    @Test
    public void copyBeanToMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtils.copy(person, map);
        System.out.println(map);
    }

    @Test
    public void copyMapToBean() {
        Student student = new Student();
        BeanUtils.copy(personMap, student);
        System.out.println(student);
    }

    @Test
    public void copyBeanToBean() {
        Student student = new Student();
        BeanUtils.copy(person, student);
        System.out.println(student);
    }

}
