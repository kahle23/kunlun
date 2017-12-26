package com.apyhs.artoria.collection;

import org.junit.Test;

import java.util.Map;

public class HashMapTest {

    @Test
    public void test1() throws Exception {
        Map<String, String> map = new java.util.HashMap<String, String>();
        map.put("aaa", "bbb");
        System.out.println(new HashMap<String, String>(map).set("123", "456"));
        System.out.println(new HashMap<String, String>(map).set("111", "222"));
    }

}
