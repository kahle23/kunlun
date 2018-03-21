package com.github.kahlkn.artoria.beans;

import com.alibaba.fastjson.JSON;
import com.github.kahlkn.artoria.entity.Student;
import com.github.kahlkn.artoria.util.RandomUtils;
import org.junit.Test;

public class JdkBeanMapTest {
    private Student student = RandomUtils.nextObject(Student.class);

    @Test
    public void test1() {
        BeanMap map = new JdkBeanMap();
        map.setBean(student);
        System.out.println(JSON.toJSONString(student));
        System.out.println(map);
        System.out.println(map.put("name", "lisi"));
        System.out.println(map);
        System.out.println(JSON.toJSONString(student));
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
