package artoria.identity;

import org.junit.Test;

public class IdUtilsTest {

    @Test
    public void test1() {
        for (int i = 0; i < 100; i++) {
            Number number = IdUtils.nextNumber();
            System.out.print(number);
            System.out.print("  " + number.toString().length());
            System.out.println();
        }
    }

    @Test
    public void test2() {
        System.out.println(IdUtils.nextString());
        System.out.println(IdUtils.nextNumber());
    }

}
