package com.apyhs.artoria.beans;

import org.junit.Before;
import org.junit.Test;

public class JdkBeanMapTest {
    private Student student = new Student();

    @Before
    public void init() {
        student.setName("zhangsan");
        student.setAge(19);
        student.setHeight(170d);
        student.setEmail("zhangsan@email.com");
        student.setScore(99);
    }

    @Test
    public void test1() {
        BeanMap map = new JdkBeanMap();
        map.setBean(student);
        System.out.println(student);
        System.out.println(map);
        System.out.println(map.put("name", "lisi"));
        System.out.println(map);
        System.out.println(student);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        BeanMap map = new JdkBeanMap(student);
        System.out.println(map);

        BeanMap newMap = (JdkBeanMap) map.clone();
        System.out.println(student == newMap.getBean());
        newMap.put("name", "lisi");
        System.out.println(newMap);
    }

}
