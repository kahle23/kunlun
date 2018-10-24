package artoria.collection;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class ReferenceHashMapTest {

    @Test
    public void test0() throws Exception {
        Map<String, Object> map = new ReferenceHashMap<String, Object>(ReferenceHashMap.Type.SOFT);
        map.put("1", "val[1");
        map.put("2", "val[2");
        map.put("3", "val[3");
        System.out.println(map.size());
        System.out.println(map.containsKey("1"));
        System.out.println(map.isEmpty());
        System.out.println(map.get("2"));
        System.out.println(map);
        System.out.println(map.remove("1"));
        System.out.println(map.get("1"));
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey() + " | " + entry.getValue());
        }
    }

    @Test
    public void test1() throws Exception {
        Map<String, Object> map = new ReferenceHashMap<String, Object>(ReferenceHashMap.Type.WEAK, 0);
        map.put("test1", new Object());
        System.out.println(map.get("test1"));
        System.gc();
        System.out.println(map.get("test1"));
    }

    @Test
    @Ignore
    public void test2() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 999999; i++) {
            builder.append(i);
        }
        String content = builder.toString();
        Map<String, Object> map = new ReferenceHashMap<String, Object>(ReferenceHashMap.Type.SOFT, 0);
//        Map<String, Object> map = new HashMap<String, Object>();
        for (long i = 0; i < 999999; i++) {
            map.put(i + "", content + i);
        }
        System.out.println(map.get("1"));
    }

    @Test
    public void test3() throws Exception {
        Map<String, Object> map = new ReferenceHashMap<String, Object>(ReferenceHashMap.Type.WEAK, 1);
        for (int i = 0; i < 100; i++) {
            map.put("data" + i, "data - - " + i);
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue());
        }
    }

}
