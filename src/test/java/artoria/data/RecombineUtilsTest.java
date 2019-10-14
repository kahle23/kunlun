package artoria.data;

import artoria.entity.Person;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.random.RandomUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecombineUtilsTest {
    private static Logger log = LoggerFactory.getLogger(RecombineUtilsTest.class);
    private List<Person> list = new ArrayList<Person>();

    @Before
    public void init() {
        list.add(null);
        list.add(RandomUtils.nextObject(Person.class));
        list.add(RandomUtils.nextObject(Person.class));
        list.add(RandomUtils.nextObject(Person.class));
        list.add(null);
        list.add(RandomUtils.nextObject(Person.class));
        list.add(RandomUtils.nextObject(Person.class));
    }

    @Test
    public void testListToListList() {
        List<List<Person>> lists = RecombineUtils.listToListList(list, 2);
        for (List<Person> people : lists) {
            log.info(JSON.toJSONString(people));
        }
    }

    @Test
    public void testListToListProperty() {
        List<String> list = RecombineUtils.listToListProperty(this.list, "name", String.class);
        log.info(JSON.toJSONString(list, true));
    }

    @Test
    public void testListToMapBean() {
        Map<String, Person> map = RecombineUtils.listToMapBean(list, "name");
        log.info(JSON.toJSONString(map, true));
    }

    @Test
    public void testListToMapList() {
        Map<String, List<Person>> map = RecombineUtils.listToMapList(list, "name");
        log.info(JSON.toJSONString(map, true));
    }

    @Test
    public void testListToMapProperty() {
        Map<String, Object> map = RecombineUtils.listToMapProperty(list, "age", "name");
        log.info(JSON.toJSONString(map, true));
    }

}
