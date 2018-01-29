package com.apyhs.artoria.collection;

import org.junit.Test;

import java.util.Map;

public class SoftHashMapTest {

    @Test
    public void test1() throws Exception {
        Map<String, Object> map = new SoftHashMap<String, Object>();
        map.put("test1", "test1Value");
        System.out.println(map.get("test1"));
        System.out.println(map.get("none"));
    }

    @Test
    public void test2() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 999999; i++) {
            builder.append(i);
        }
        String content = builder.toString();
        SoftHashMap<String, Object> map = new SoftHashMap<String, Object>();
//        HashMap<String, Object> map = new HashMap<String, Object>();
        for (long i = 0; i < 999999; i++) {
            map.put(i + "", new String(content + i));
        }
        System.out.println(map.get("1"));
    }

}
