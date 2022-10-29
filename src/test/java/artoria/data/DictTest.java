package artoria.data;

import artoria.data.bean.BeanUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.mock.MockUtils;
import artoria.test.bean.User;
import org.junit.Test;

import java.util.Map;

public class DictTest {
    private static Logger log = LoggerFactory.getLogger(DictTest.class);

    @Test
    public void test1() {
        Dict dict = new Dict();
        Dict dict1 = Dict.of();
    }

    @Test
    public void test2() {
        Dict dict = Dict.of("name", null).set("age", "19");
        log.info("{}", dict);
    }

    @Test
    public void test3() {
        Dict dict = Dict.of("name", "Superman").set("age", "19");
        String name = dict.getString("name");
        Integer age = dict.getInteger("age");
        log.info("name: {}, age: {}", name, age);
    }

    @Test
    public void test4() {
        Dict dict = Dict.of("data", Dict.of("k1", "v1")).set("count", "30");
        Map map = dict.get("data", Map.class);
        Integer count = dict.getInteger("count");
        log.info("data: {}, age: {}", map, count);
        //Map map1 = dict.get("count", Map.class);
    }

    @Test
    public void test5() {
        Map<String, Object> map = BeanUtils.beanToMap(MockUtils.mock(User.class));
        Dict dict = Dict.of("data", map).set("count", "30");
        log.info("{}", dict);
        Dict data = dict.getDict("data");
        log.info("{}", data);
    }

}
