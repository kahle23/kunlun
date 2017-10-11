package sabertest.util.codec;

import org.junit.Test;
import saber.util.codec.UrlUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UrlUtilsTest {

    @Test
    public void test2() throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("r", "1234567890");
        map.put("q", "你好，Java！");
        map.put("天寒地冻", "春暖花开");
        map.put("我是键", "你好，世界！");
        String s = UrlUtils.encode(map, "utf-8");
        System.out.println(s);
        Map<String, String> map1 = UrlUtils.decode(s, "utf-8");
        System.out.println(map1.toString());
    }

}
