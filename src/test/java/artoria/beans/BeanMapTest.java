package artoria.beans;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.mock.MockUtils;
import artoria.test.bean.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class BeanMapTest {
    private static Logger log = LoggerFactory.getLogger(BeanMapTest.class);
    private User user = MockUtils.mock(User.class);

    @Test
    public void test1() {
        BeanMap map = new SimpleBeanMap();
        map.setBean(user);
        log.info(JSON.toJSONString(user));
        log.info("{}", map);
        log.info("{}", map.put("name", "lisi"));
        log.info("{}", map);
        log.info(JSON.toJSONString(user));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        BeanMap map = new SimpleBeanMap(user);
        log.info("{}", map);

        BeanMap newMap = (SimpleBeanMap) map.clone();
        log.info("{}", user == newMap.getBean());
        newMap.put("name", "lisi");
        log.info("{}", newMap);
    }

}
