package apyh.artoria.util;

import org.junit.Test;

public class RandomUtilsTest {

    @Test
    public void test_nextInt() {
        int bound = 100;
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
        System.out.println(RandomUtils.nextInt(bound));
    }

    @Test
    public void test_nextUUID() {
        System.out.println(RandomUtils.nextUUID());
        System.out.println(RandomUtils.nextUUID(null));
        System.out.println(RandomUtils.nextUUID(""));
        System.out.println(RandomUtils.nextUUID("-"));
        System.out.println(RandomUtils.nextUUID("+"));
    }

    @Test
    public void test_nextBigDecimal() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(RandomUtils.nextBigDecimal(1000));
        }
    }

}
