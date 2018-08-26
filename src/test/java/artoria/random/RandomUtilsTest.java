package artoria.random;

import artoria.entity.Menu;
import artoria.entity.Student;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class RandomUtilsTest {

    @Test
    public void testNextInt() {
        int bound = 100;
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomUtils.nextInt());
            System.out.println(RandomUtils.nextInt(bound));
        }
    }

    @Test
    public void testNextLong() {
        for (int i = 0; i < 20; i++) {
            System.out.println(RandomUtils.nextLong());
        }
    }

    @Test
    public void testNextFloat() {
        for (int i = 0; i < 20; i++) {
            System.out.println(RandomUtils.nextFloat());
        }
    }

    @Test
    public void testNextDouble() {
        for (int i = 0; i < 20; i++) {
            System.out.println(RandomUtils.nextDouble());
        }
    }

    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 20; i++) {
            System.out.println(RandomUtils.nextBoolean());
        }
    }

    @Test
    public void testNextObject() {
        Student student = RandomUtils.nextObject(Student.class);
        System.out.println(JSON.toJSONString(student, true));

        Menu menu = RandomUtils.nextObject(Menu.class);
        System.out.println(JSON.toJSONString(menu, true));
    }

}
