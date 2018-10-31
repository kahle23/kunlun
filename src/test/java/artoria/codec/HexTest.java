package artoria.codec;

import org.junit.Test;

import java.util.Arrays;

public class HexTest {
    private Hex hex = Hex.getInstance(true);

    @Test
    public void test1() {
        byte[] dataWillEncode1 = {12, 2, 54, -32, 12, 21, 23, -26, -65, 32, 51, 11, -7, 71};
        String dataWillDecode1 = "0C0236E00C1517E6BF20330BF947";
        System.out.println("byte array: " + Arrays.toString(dataWillEncode1));
        System.out.println("string: " + dataWillDecode1);
        System.out.println(hex.encodeToString(dataWillEncode1));
        System.out.println(Arrays.toString(hex.decodeFromString(dataWillDecode1)));
    }

    @Test
    public void test2() {
        byte[] dataWillEncode2 = {54, -32, 65, -8, 23, -67, 43, 34, -4, 34, -64, 56, 34, 22};
        String dataWillDecode2 = "36E041F817BD2B22FC22C0382216";
        System.out.println("byte array: " + Arrays.toString(dataWillEncode2));
        System.out.println("string: " + dataWillDecode2);
        System.out.println(hex.encodeToString(dataWillEncode2));
        System.out.println(Arrays.toString(hex.decodeFromString(dataWillDecode2)));
    }

}
