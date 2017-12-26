package com.apyhs.artoria.codec;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UrlCodecTest {

    private Map<String, String> data = new HashMap<String, String>();

    @Before
    public void init() {
        data.put("name", "zhangsan");
        data.put("age", "19");
        data.put("email", "zhangsan@email.com");
        data.put("blog", "http://zhangsan.com");
    }

    @Test
    public void test1() throws Exception {
        String encode = UrlCodec.ME.encodeToString(data);
        System.out.println(encode);
        Map<String, String> map = UrlCodec.ME.decodeFromString(encode);
        System.out.println(map.toString());
    }

    @Test
    public void test2() throws Exception {
        UrlCodec urlCodec = UrlCodec.create("utf-8");
        String encode = urlCodec.encodeToString(data);
        System.out.println(encode);
        Map<String, String> decode = urlCodec.decodeFromString(encode);
        System.out.println(decode);
    }

}
