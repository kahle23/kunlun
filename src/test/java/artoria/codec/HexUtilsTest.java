package artoria.codec;

import org.junit.Test;

import java.util.Arrays;

public class HexUtilsTest {

    @Test
    public void test1() {
        HexUtils.setDefaultToUpperCase(true);
        byte[] data = {12, 2, 54, -32, 12, 21, 23, -26, -65, 32, 51, 11, -7, 71};
        System.out.println(Arrays.toString(data));
        String hexString = HexUtils.encodeToString(data);
        byte[] decode = HexUtils.decodeFromString(hexString);
        System.out.println(hexString);
        System.out.println(Arrays.toString(decode));
    }

}
