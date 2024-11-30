package kunlun.crypto;

import kunlun.codec.CodecUtils;
import kunlun.file.Text;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;

import static kunlun.codec.CodecUtils.BASE64;

public class CryptoUtilsCustomTest {
    private static final Logger log = LoggerFactory.getLogger(CryptoUtilsCustomTest.class);
    private static final String data = "Hello, Java! ";

    @Test
    public void test1() {
        byte[] encrypt = CryptoUtils.encrypt(data.getBytes());
        log.info("Encrypt base64: {}", CodecUtils.encodeToString(BASE64, encrypt));
        log.info("Decrypt string: {}", CryptoUtils.decryptToString(encrypt));
    }

    @Test
    public void test2() throws IOException {
        Text text = new Text();
        text.readFromClasspath("logging.properties");
        byte[] encrypt = CryptoUtils.encrypt(text.writeToByteArray());
        log.info("Encrypt base64: {}", CodecUtils.encodeToString(BASE64, encrypt));
        log.info("Decrypt string: {}", CryptoUtils.decryptToString(encrypt));
    }

    @Test
    public void test3() {

        log.info("Digest base64: {}", CryptoUtils.digestToBase64(data));
    }

}
