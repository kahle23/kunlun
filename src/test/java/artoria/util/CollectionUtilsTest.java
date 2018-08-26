package artoria.util;

import artoria.random.RandomUtils;
import com.alibaba.fastjson.JSON;
import artoria.entity.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtilsTest {
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
        Person person = CollectionUtils.takeFirstNotNullElement(list);
        System.out.println(JSON.toJSONString(person, true));
    }

    @Test
    public void sortTest() {
        CollectionUtils.sort(list, true, "age", "height", "weight");
        System.out.println(JSON.toJSONString(list, true));
    }

}
