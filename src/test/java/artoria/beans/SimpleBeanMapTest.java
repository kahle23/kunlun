package artoria.beans;

import artoria.entity.Student;
import artoria.random.RandomUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class SimpleBeanMapTest {
    private Student student = RandomUtils.nextObject(Student.class);

    @Test
    public void test1() {
        BeanMap map = new SimpleBeanMap();
        map.setBean(student);
        System.out.println(JSON.toJSONString(student));
        System.out.println(map);
        System.out.println(map.put("name", "lisi"));
        System.out.println(map);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        BeanMap map = new SimpleBeanMap(student);
        System.out.println(map);

        BeanMap newMap = (SimpleBeanMap) map.clone();
        System.out.println(student == newMap.getBean());
        newMap.put("name", "lisi");
        System.out.println(newMap);
    }

}
