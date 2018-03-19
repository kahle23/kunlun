package com.github.kahlkn.artoria.util;

import com.alibaba.fastjson.JSON;
import com.github.kahlkn.artoria.entity.Menu;
import com.github.kahlkn.artoria.entity.Student;
import org.junit.Test;

public class RandomUtilsTest {

    @Test
    public void testNextInt() {
        int bound = 100;
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt());
        System.out.println(RandomUtils.nextInt());
        System.out.println(RandomUtils.nextInt());
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
    public void testtt() {
        Menu student = RandomUtils.nextObject(Menu.class);
        System.out.println(JSON.toJSONString(student));
    }

}
