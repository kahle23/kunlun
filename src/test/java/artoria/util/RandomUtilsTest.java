package artoria.util;

import com.alibaba.fastjson.JSON;
import artoria.entity.Menu;
import artoria.entity.Student;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class RandomUtilsTest {

    @Test
    public void testNextInt() {
        int bound = 100;
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
    }

    @Test
    public void testNextUUID() {
        System.out.println(RandomUtils.nextUUID());
        System.out.println(RandomUtils.nextUUID(null));
        System.out.println(RandomUtils.nextUUID(""));
        System.out.println(RandomUtils.nextUUID("-"));
        System.out.println(RandomUtils.nextUUID("+"));
    }

    @Test
    public void testNextBigDecimal() {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomUtils.nextBigDecimal(1000));
        }
    }

    @Test
    public void testNextObject() {
        Student student = RandomUtils.nextObject(Student.class);
        System.out.println(JSON.toJSONString(student, true));

        Menu menu = RandomUtils.nextObject(Menu.class);
        System.out.println(JSON.toJSONString(menu, true));
    }

    @Test
    public void testNextArray() {
        Student[] students = RandomUtils.nextArray(Student.class);
        System.out.println(JSON.toJSONString(students, true));

        Menu[] menus = RandomUtils.nextObject(Menu[].class);
        System.out.println(JSON.toJSONString(menus, true));
    }

    @Test
    public void testNextList() {
        List<Student> students = RandomUtils.nextList(Student.class);
        System.out.println(JSON.toJSONString(students, true));
    }

    @Test
    public void testNextMap() {
        Map<String, Student> map = RandomUtils.nextMap(String.class, Student.class);
        System.out.println(JSON.toJSONString(map, true));
    }

}
