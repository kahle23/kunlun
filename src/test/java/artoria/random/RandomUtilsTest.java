package artoria.random;

import artoria.entity.Menu;
import artoria.entity.Student;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static artoria.common.Constants.*;

public class RandomUtilsTest {
    private static Logger log = LoggerFactory.getLogger(RandomUtilsTest.class);

    @Test
    public void testNextInt() {
        for (int i = ZERO; i < ONE_HUNDRED; i++) {
            log.info("{}", RandomUtils.nextInt());
            log.info("{}", RandomUtils.nextInt(ONE_HUNDRED));
        }
    }

    @Test
    public void testNextLong() {
        for (int i = ZERO; i < TWENTY; i++) {
            log.info("{}", RandomUtils.nextLong());
        }
    }

    @Test
    public void testNextFloat() {
        for (int i = ZERO; i < TWENTY; i++) {
            log.info("{}", RandomUtils.nextFloat());
        }
    }

    @Test
    public void testNextDouble() {
        for (int i = ZERO; i < TWENTY; i++) {
            log.info("{}", RandomUtils.nextDouble());
        }
    }

    @Test
    public void testNextBoolean() {
        for (int i = ZERO; i < TWENTY; i++) {
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
