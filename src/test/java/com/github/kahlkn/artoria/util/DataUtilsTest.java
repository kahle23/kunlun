package com.github.kahlkn.artoria.util;

import com.alibaba.fastjson.JSON;
import com.github.kahlkn.artoria.entity.Person;
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
        list.add(RandomUtils.nextObject(Person.class));
        list.add(RandomUtils.nextObject(Person.class));
        list.add(RandomUtils.nextObject(Person.class));
        list.add(null);
        list.add(RandomUtils.nextObject(Person.class));
        list.add(RandomUtils.nextObject(Person.class));
    }

    @Test
    public void testTakeFirstNotNullElement() {
        Person person = DataUtils.takeFirstNotNullElement(list);
        System.out.println(JSON.toJSONString(person, true));
    }

    @Test
    public void testListToListList() {
        List<List<Person>> lists = DataUtils.listToListList(list, 2);
        for (List<Person> people : lists) {
            System.out.println(JSON.toJSONString(people));
        }
    }

    @Test
    public void testListToMapBean() {
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
