package com.apyhs.artoria.beans;

import com.apyhs.artoria.entity.Person;
import com.apyhs.artoria.entity.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanHandlerTest {
    private static final Person PERSON = new Person();
    private static final Map<String, Object> PERSON_MAP = new HashMap<String, Object>();
    private static final List<Person> PERSONS = new ArrayList<Person>();
    private static final List<Map<String, Object>> PERSONS_MAP;

    static {
        PERSON.setName("zhangsan");
        PERSON.setAge(21);
        PERSON.setHeight(175D);
        PERSONS.add(PERSON);

        BeanUtils.copy(PERSON, PERSON_MAP);

        Person lisi = new Person();
        lisi.setName("lisi");
        lisi.setAge(19);
        lisi.setHeight(169D);
        PERSONS.add(lisi);

        PERSONS_MAP = BeanUtils.beanToMapInList(PERSONS);
    }

    @Test
    public void mapToBean() throws Exception {
        Student student = BeanUtils.mapToBean(PERSON_MAP, Student.class);
        System.out.println(student);
    }

    @Test
    public void mapToBean1() throws Exception {
        Student student = new Student();
        BeanUtils.mapToBean(PERSON_MAP, student);
        System.out.println(student);
    }

    @Test
    public void beanToMap() throws Exception {
        Map<String, Object> map = BeanUtils.beanToMap(PERSON);
        System.out.println(map);
    }

    @Test
    public void beanToBean() throws Exception {
        Student student = BeanUtils.beanToBean(PERSON, Student.class);
        System.out.println(student);
    }

    @Test
    public void beanToBean1() throws Exception {
        Student student = new Student();
        BeanUtils.beanToBean(PERSON, student);
        System.out.println(student);
    }

    @Test
    public void mapToBeanInList() throws Exception {
        System.out.println(PERSONS_MAP);
        List<Student> students = BeanUtils.mapToBeanInList(PERSONS_MAP, Student.class);
        System.out.println(students);
    }

    @Test
    public void beanToMapInList() throws Exception {
        List<Map<String, Object>> list = BeanUtils.beanToMapInList(PERSONS);
        System.out.println(list);
    }

    @Test
    public void beanToBeanInList() throws Exception {
        List<Student> students = BeanUtils.beanToBeanInList(PERSONS, Student.class);
        System.out.println(students);
    }

}