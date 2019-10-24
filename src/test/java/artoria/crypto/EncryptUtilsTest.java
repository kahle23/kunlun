package artoria.crypto;

import artoria.codec.Base64Utils;
import artoria.file.Text;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;

public class EncryptUtilsTest {
    private static Logger log = LoggerFactory.getLogger(EncryptUtilsTest.class);
    private static String data = "Hello, Java! ";

    @Test
    public void test1() {
        byte[] encrypt = EncryptUtils.encrypt(data.getBytes());
        log.info("Encrypt base64: {}", Base64Utils.encodeToString(encrypt));
        byte[] decrypt = EncryptUtils.decrypt(encrypt);
        log.info("Decrypt string: {}", decrypt != null ? new String(decrypt) : null);
    }

    @Test
    public void test2() throws IOException {
        Text text = new Text();
        text.readFromClasspath("logging.properties");
        byte[] encrypt = EncryptUtils.encrypt(text.writeToByteArray());
        log.info("Encrypt base64: {}", Base64Utils.encodeToString(encrypt));
        byte[] decrypt = EncryptUtils.decrypt(encrypt);
        log.info("Decrypt string: {}", decrypt != null ? new String(decrypt) : null);
    }

    @Test
    public void test3() {
        byte[] bytes = EncryptUtils.digest(data.getBytes());
        log.info("Digest base64: {}", Base64Utils.encodeToString(bytes));
    }

    @Test
    public void test4() {
        byte[] bytes = EncryptUtils.digest256(data.getBytes());
        log.info("Digest 256 base64: {}", Base64Utils.encodeToString(bytes));
    }

    @Test
    public void test5() {
        byte[] bytes = EncryptUtils.digest512(data.getBytes());
        log.info("Digest 512 base64: {}", Base64Utils.encodeToString(bytes));
    }

}
