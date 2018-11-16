package artoria.collection;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class ReferenceHashMapTest {
    private static Logger log = LoggerFactory.getLogger(ReferenceHashMapTest.class);

    @Test
    public void test0() throws Exception {
        Map<String, Object> map = new ReferenceHashMap<String, Object>(ReferenceHashMap.Type.SOFT);
        map.put("1", "val[1");
        map.put("2", "val[2");
        map.put("3", "val[3");
        log.info("" + map.size());
        log.info("" + map.containsKey("1"));
        log.info("" + map.isEmpty());
        log.info("" + map.get("2"));
        log.info("" + map);
        log.info("" + map.remove("1"));
        log.info("" + map.get("1"));
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            log.info(entry.getKey() + " | " + entry.getValue());
        }
    }

    @Test
    public void test1() throws Exception {
        Map<String, Object> map = new ReferenceHashMap<String, Object>(ReferenceHashMap.Type.WEAK, 0);
        map.put("test1", new Object());
        log.info("" + map.get("test1"));
        System.gc();
        log.info("" + map.get("test1"));
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
        log.info("" + map.get("1"));
    }

    @Test
    public void test3() throws Exception {
        Map<String, Object> map = new ReferenceHashMap<String, Object>(ReferenceHashMap.Type.WEAK, 1);
        for (int i = 0; i < 100; i++) {
            map.put("data" + i, "data - - " + i);
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            log.info(entry.getKey() + " | " + entry.getValue());
        }
    }

}
