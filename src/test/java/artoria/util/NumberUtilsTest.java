package artoria.util;

import org.junit.Test;

import java.math.BigDecimal;

public class NumberUtilsTest {

    @Test
    public void test1() {
        System.out.println(NumberUtils.round(578.4000345f));
        System.out.println(NumberUtils.round(578.44325f).doubleValue() + 666);
        System.out.println(NumberUtils.round(1578.478545f));
        System.out.println(NumberUtils.round(12578.4455f));
        double round = NumberUtils.round(123578.4455f).doubleValue();
        BigDecimal decimal = new BigDecimal(round);
        System.out.println(decimal);
        decimal = BigDecimal.valueOf(round);
        System.out.println(decimal);
    }

    @Test
    public void test2() {
        System.out.println(NumberUtils.format(99, "00000"));
        System.out.println(NumberUtils.format(8754.65638, ".000"));
        System.out.println(NumberUtils.format(8754.65638, "0000000.000"));
        System.out.println(NumberUtils.format(8754.60000, ".000"));
        System.out.println(NumberUtils.format(99, "#######"));
        System.out.println(NumberUtils.format(8754.65638, "#######.###"));
        System.out.println(NumberUtils.format(8754.60000, "#######.##"));
    }

}
