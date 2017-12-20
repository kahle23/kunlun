package apyh.artoria.codec;

import org.junit.Test;

import java.util.Arrays;

public class HexTest {

    @Test
    public void test1() {
        byte[] data = {12, 2, 54, -32, 12, 21, 23, -26, -65, 32, 51, 11, -7, 71};
        System.out.println(Arrays.toString(data));
        String hexString = Hex.ME.encodeToString(data);
        byte[] decode = Hex.ME.decodeFromString(hexString);
        System.out.println(hexString);
        System.out.println(Arrays.toString(decode));
    }


}
