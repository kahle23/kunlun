package com.github.kahlkn.artoria.util;

import com.alibaba.fastjson.JSON;
import com.github.kahlkn.artoria.entity.EntityUtils;
import com.github.kahlkn.artoria.entity.Person;
import com.github.kahlkn.artoria.entity.Student;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataUtilsTest {
    private List<Person> list = new ArrayList<Person>();

    @Before
    public void init() {
        list.add(null);
        list.add(EntityUtils.getZhangSan());
        list.add(EntityUtils.getZhangSan());
        list.add(EntityUtils.getLisi());
        list.add(null);
        Student bean = EntityUtils.getZhangSan();
        bean.setName("wangwu");
        list.add(bean);
        bean = EntityUtils.getZhangSan();
        bean.setName("wangwu");
        list.add(bean);
    }

    @Test
    public void testTakeFirstNotNullElement() {
        Person person = DataUtils.takeFirstNotNullElement(list);
        System.out.println(JSON.toJSONString(person, true));
    }

    @Test
    public void listToListList() {
        List<List<Person>> lists = DataUtils.listToListList(list, 2);
        for (List<Person> people : lists) {
            System.out.println(JSON.toJSONString(people));
        }
    }

    @Test
    public void listToMapBean() {
        Map<String, Person> map = DataUtils.listToMapBean(list, "name");
        System.out.println(JSON.toJSONString(map, true));
    }

    @Test
    public void testListToMapList() {
        Map<String, List<Person>> map = DataUtils.listToMapList(list, "name");
        System.out.println(JSON.toJSONString(map, true));
    }

    @Test
    public void testListToListProperty() {
        List<String> list = DataUtils.listToListProperty(this.list, "name", String.class);
        System.out.println(JSON.toJSONString(list, true));
    }

}
