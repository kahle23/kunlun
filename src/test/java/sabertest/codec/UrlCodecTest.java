package sabertest.codec;

import org.junit.Test;
import saber.codec.UrlCodec;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UrlCodecTest {

    @Test
    public void test1() throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("r", "1234567890");
        map.put("q", "你好，Java！");
        map.put("天寒地冻", "春暖花开");
        map.put("我是键", "你好，世界！");
        String s = UrlCodec.ME.encode(map);
        System.out.println(s);
        Map<String, String> map1 = UrlCodec.ME.decode(s);
        System.out.println(map1.toString());
    }

}
