package sabertest.util;

import org.junit.Test;
import saber.util.JMap;

import java.util.HashMap;
import java.util.Map;

public class JMapTest {

    @Test
    public void test1() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "bbb");
        System.out.println(JMap.on().setCode(0).setData(map));
        System.out.println(JMap.on().setCode(2010).setData("hello!"));
    }

}
