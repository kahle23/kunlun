package artoria.util;

import artoria.entity.Person;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.random.RandomUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtilsTest {
    private static Logger log = LoggerFactory.getLogger(CollectionUtilsTest.class);
    private List<Person> list = new ArrayList<Person>();

    @Before
    public void init() {
        list.add(null);
        list.add(RandomUtils.nextObject(Person.class));
        list.add(null);
        list.add(RandomUtils.nextObject(Person.class));
        list.add(RandomUtils.nextObject(Person.class));
    }

    @Test
    public void testTakeFirstNotNullElement() {
        Person person = CollectionUtils.getFirstNotNullElement(list);
        log.info(JSON.toJSONString(person, true));
    }

}
