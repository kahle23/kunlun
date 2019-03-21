package artoria.beans;

import artoria.entity.Student;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.random.RandomUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class BeanMapTest {
    private static Logger log = LoggerFactory.getLogger(BeanMapTest.class);
    private Student student = RandomUtils.nextObject(Student.class);

    @Test
    public void test1() {
        BeanMap map = new SimpleBeanMap();
        map.setBean(student);
        log.info(JSON.toJSONString(student));
        log.info("{}", map);
        log.info("{}", map.put("name", "lisi"));
        log.info("{}", map);
        log.info(JSON.toJSONString(student));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        BeanMap map = new SimpleBeanMap(student);
        log.info("{}", map);

        BeanMap newMap = (SimpleBeanMap) map.clone();
        log.info("{}", student == newMap.getBean());
        newMap.put("name", "lisi");
        log.info("{}", newMap);
    }

}
