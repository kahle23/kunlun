package com.github.kahlkn.artoria.net;

import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Ignore
public class HttpUtilsTest {
    private String testUrl = "https://kahlkn.github.io";
    private String testUrl1 = "https://www.github.com";
    private String testUrl2 = "https://www.bing.com";

    @Test
    public void test1() throws Exception {
        System.out.println(HttpUtils.create(testUrl).get());
        System.out.println(HttpUtils.create(testUrl1).get());
        System.out.println(HttpUtils.create(testUrl2).get());
    }

    @Test
    public void test2() throws Exception {
        System.out.println(HttpUtils.create(testUrl).get());
        System.out.println(HttpUtils.create(testUrl).post());
        System.out.println(HttpUtils.create(testUrl).put());
        System.out.println(HttpUtils.create(testUrl).delete());
        System.out.println(HttpUtils.create(testUrl).head());
        System.out.println(HttpUtils.create(testUrl).options());
        System.out.println(HttpUtils.create(testUrl).setMethod("GET").execute());
    }

    @Test
    public void test3() throws Exception {
        HttpUtils http = HttpUtils.create();
        System.out.println(http.setUrl(testUrl).get());
        http.setConnectTimeout(1000).setReadTimeout(1000)
                .setUserAgent(">>>>>>>>>>>>>>>>>>>>>").setCharset("utf-8");
        System.out.println(http.setUrl(testUrl1).get());
        System.out.println(http.setUrl(testUrl2).get());
    }

    @Test
    public void test4() throws Exception {
        String url = "http://ip.chinaz.com/getip.aspx";
        System.out.println(HttpUtils.create(url)
                .setHttpProxy("127.0.0.1", 5000)
                .setProxyAuth("zhangsan", "123")
                .get());
    }

    @Test
    public void test5() throws Exception {
        String url = "https://";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("labelName", "");
        data.put("categoryId", "");
        data.put("pageNum", "1");
        data.put("pageSize", "-1");
        String post = HttpUtils.create(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "")
                .setData(JSON.toJSONString(data).getBytes())
                .post();
        System.out.println(JSON.toJSONString(JSON.parseObject(post), true));
    }

}
