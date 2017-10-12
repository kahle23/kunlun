package sabertest.codec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.junit.Test;
import saber.codec.BarcodeUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BarcodeUtilsTest {

    @Test
    public void test1() throws WriterException, IOException {
        BitMatrix bitMatrix = BarcodeUtils.encode("http://uux.me", BarcodeFormat.QR_CODE, 500, 500);
        BufferedImage image = BarcodeUtils.toBufferedImage(bitMatrix, BufferedImage.TYPE_INT_RGB);
        boolean b = ImageIO.write(image, "jpg", new File("C:\\Users\\Kahle\\Desktop\\123.jpg"));
        System.out.println(b);
    }

    @Test
    public void test2() throws WriterException, IOException {
        BufferedImage image = BarcodeUtils.encodeToImage("http://uux.me", BarcodeFormat.QR_CODE, 500, 500, BufferedImage.TYPE_INT_RGB);
        boolean b = ImageIO.write(image, "jpg", new File("C:\\Users\\Kahle\\Desktop\\123.jpg"));
        System.out.println(b);
    }

    @Test
    public void test3() throws WriterException, IOException {
        BufferedImage image = BarcodeUtils.encodeToImage("1234567890", BarcodeFormat.QR_CODE, 500, 100, BufferedImage.TYPE_INT_RGB);
        boolean b = ImageIO.write(image, "jpg", new File("C:\\Users\\Kahle\\Desktop\\123.jpg"));
        System.out.println(b);
    }

    @Test
    public void test4() throws NotFoundException, IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\Kahle\\Desktop\\123.jpg"));
        Result result = BarcodeUtils.decode(image);
        System.out.println(result.getText());
        System.out.println(result.getBarcodeFormat());
    }

    @Test
    public void test5() throws NotFoundException, IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\Kahle\\Desktop\\123.jpg"));
        System.out.println(BarcodeUtils.decodeToText(image));
    }

    @Test
    public void test6() throws NotFoundException, IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\Kahle\\Desktop\\123.jpg"));
        System.out.println(BarcodeUtils.decodeToText(image));
    }

}
