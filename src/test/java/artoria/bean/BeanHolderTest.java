package artoria.bean;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.mock.MockUtils;
import artoria.test.pojo.entity.animal.Cat;
import artoria.test.pojo.entity.animal.Dog;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Arrays;

public class BeanHolderTest {
    private static Logger log = LoggerFactory.getLogger(BeanHolderTest.class);

    @Test
    public void test1() {
        Dog dog = MockUtils.mock(Dog.class);
        Cat cat = MockUtils.mock(Cat.class);
        log.info("dog is: {}", JSON.toJSONString(dog));
        log.info("cat is: {}", JSON.toJSONString(cat));
        BeanHolder.put("dog", dog);
//        BeanHolder.put("dog", cat);
        BeanHolder.put("dog1", dog);
        BeanHolder.put("cat", cat);
        BeanHolder.put("cat1", cat);
        //log.info(JSON.toJSONString(BeanHolder.get(Dog.class)));
//        Cat cat = BeanHolder.get("dog", Cat.class);
//        log.info(JSON.toJSONString(cat));
        log.info("Dog names: {}", Arrays.toString(BeanHolder.getNames(Dog.class)));
        log.info("Cat names: {}", Arrays.toString(BeanHolder.getNames(Cat.class)));
    }

}
