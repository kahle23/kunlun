package artoriatest.codec;

import org.junit.Before;
import org.junit.Test;
import artoria.codec.UrlCodec;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UrlCodecTest {

    private Map<String, String> data = new HashMap<>();

    @Before
    public void init() {
        data.put("name", "zhangsan");
        data.put("age", "19");
        data.put("email", "zhangsan@email.com");
        data.put("blog", "http://zhangsan.com");
    }

    @Test
    public void test1() throws Exception {
        String encode = UrlCodec.ME.encode(data);
        System.out.println(encode);
        Map<String, String> map = UrlCodec.ME.decode(encode);
        System.out.println(map.toString());
    }

    @Test
    public void test2() throws Exception {
        UrlCodec urlCodec = UrlCodec.create("utf-8");
        String encode = urlCodec.encode(data);
        System.out.println(encode);
        Map<String, String> decode = urlCodec.decode(encode);
        System.out.println(decode);
    }

}
