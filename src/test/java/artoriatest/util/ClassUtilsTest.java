package artoriatest.util;

import org.junit.Test;

import java.lang.reflect.Array;

public class ClassUtilsTest {

    @Test
    public void test1() {
        byte[] bytes = new byte[10];
        System.out.println(bytes.getClass());
        String[] strings = new String[10];
        System.out.println(strings.getClass());
        System.out.println(Array.newInstance(String.class, 0).getClass());
    }

}
