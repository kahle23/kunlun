package artoria.random;

import artoria.entity.Menu;
import artoria.entity.Student;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class RandomUtilsTest {
    private static Logger log = LoggerFactory.getLogger(RandomUtilsTest.class);

    @Test
    public void testNextInt() {
        int bound = 100;
        for (int i = 0; i < 100; i++) {
            log.info("{}", RandomUtils.nextInt());
            log.info("{}", RandomUtils.nextInt(bound));
        }
    }

    @Test
    public void testNextLong() {
        for (int i = 0; i < 20; i++) {
            log.info("{}", RandomUtils.nextLong());
        }
    }

    @Test
    public void testNextFloat() {
        for (int i = 0; i < 20; i++) {
            log.info("{}", RandomUtils.nextFloat());
        }
    }

    @Test
    public void testNextDouble() {
        for (int i = 0; i < 20; i++) {
            log.info("{}", RandomUtils.nextDouble());
        }
    }

    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 20; i++) {
            log.info("{}", RandomUtils.nextBoolean());
        }
    }

    @Test
    public void testNextObject() {
        Student student = RandomUtils.nextObject(Student.class);
        log.info(JSON.toJSONString(student, true));

        Menu menu = RandomUtils.nextObject(Menu.class);
        log.info(JSON.toJSONString(menu, true));
    }

}
