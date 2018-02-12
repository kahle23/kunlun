package com.apyhs.artoria.beans;

import com.alibaba.fastjson.JSON;
import com.apyhs.artoria.entity.EntityUtils;
import com.apyhs.artoria.entity.Person;
import com.apyhs.artoria.entity.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanHandlerTest {
    private static Person person = EntityUtils.getZhangSan();
    private static Map<String, Object> personMap = new HashMap<String, Object>();
    private static List<Person> persons = new ArrayList<Person>();
    private static List<Map<String, Object>> personsMap;

    static {
        BeanUtils.copy(person, personMap);
        persons.add(EntityUtils.getLisi());
        personsMap = BeanUtils.beanToMapInList(persons);
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
