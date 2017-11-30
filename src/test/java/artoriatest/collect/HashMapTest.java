package artoriatest.collect;

import org.junit.Test;
import artoria.collect.HashMap;

import java.util.Map;

public class HashMapTest {

    @Test
    public void test1() throws Exception {
        Map<String, String> map = new java.util.HashMap<>();
        map.put("aaa", "bbb");
        System.out.println(new HashMap<>(map).set("123", "456"));
        System.out.println(new HashMap<>(map).set("111", "222"));
    }

}
