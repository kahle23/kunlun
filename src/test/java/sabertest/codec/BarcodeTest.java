package sabertest.codec;

import com.google.zxing.BarcodeFormat;
import org.junit.Test;
import saber.codec.Barcode;

import java.io.File;

public class BarcodeTest {

    @Test
    public void test1() throws Exception {
        File file = new File("C:\\Users\\Kahle\\Desktop\\123.jpeg");
        Barcode barcode = Barcode.on(BarcodeFormat.QR_CODE, 360, 360);
        System.out.println(barcode.encodeToImage("http://uux.me", file));
        System.out.println(barcode.decodeToText(file));
    }

}
