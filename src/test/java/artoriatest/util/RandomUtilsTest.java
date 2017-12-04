package artoriatest.util;

import artoria.util.RandomUtils;
import org.junit.Test;

public class RandomUtilsTest {

    @Test
    public void test2() {
        System.out.println(RandomUtils.nextUUID());
        System.out.println(RandomUtils.nextUUID(null));
        System.out.println(RandomUtils.nextUUID(""));
        System.out.println(RandomUtils.nextUUID("-"));
        System.out.println(RandomUtils.nextUUID("+"));
    }

    @Test
    public void test3() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(RandomUtils.nextBigDecimal(1000));
        }
    }

}
