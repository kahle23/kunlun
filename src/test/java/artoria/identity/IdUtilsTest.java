package artoria.identity;

import org.junit.Test;

import static artoria.common.Constants.EMPTY_STRING;

public class IdUtilsTest {

    @Test
    public void test1() {
        for (int i = 0; i < 100; i++) {
            Long number = IdUtils.nextNumber();
            System.out.print(number);
            System.out.print("  " + number.toString().length());
            System.out.println();
        }
    }

    @Test
    public void test2() {
        SimpleIdGenerator idGenerator = new SimpleIdGenerator(EMPTY_STRING);
        IdUtils.setStringIdGenerator(idGenerator);
        for (int i = 0; i < 100; i++) {
            String string = IdUtils.nextString();
            System.out.print(string);
            System.out.print("  " + string.length());
            System.out.println();
        }
    }

}
