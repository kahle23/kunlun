package com.github.kahlkn.artoria.beans;

import com.alibaba.fastjson.JSON;
import com.github.kahlkn.artoria.entity.EntityUtils;
import com.github.kahlkn.artoria.entity.Person;
import com.github.kahlkn.artoria.entity.Student;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BeanUtilsTest {
    private static Person person = EntityUtils.getZhangSan();
    private static Map<String, Object> personMap = BeanUtils.beanToMap(EntityUtils.getLisi());

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
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void copyBeanToBean() {
        Student student = new Student();
        BeanUtils.copy(person, student);
        System.out.println(JSON.toJSONString(student));
    }

}
