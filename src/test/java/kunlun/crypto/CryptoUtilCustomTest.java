package kunlun.crypto;

import kunlun.codec.CodecUtil;
import kunlun.file.Text;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * The custom crypto tools Test.
 * @author Kahle
 */
public class CryptoUtilCustomTest {
    private static final Logger log = LoggerFactory.getLogger(CryptoUtilCustomTest.class);
    private static final String data = "Hello, Java! ";

    @Test
    public void test1() {
        byte[] encrypt = CryptoUtil.encrypt(data.getBytes());
        log.info("Encrypt base64: {}", CodecUtil.encodeToBase64(encrypt));
        log.info("Decrypt string: {}", CryptoUtil.decryptToString(encrypt));
    }

    @Test
    public void test2() throws IOException {
        Text text = new Text();
        text.readFromClasspath("logging.properties");
        byte[] encrypt = CryptoUtil.encrypt(text.writeToByteArray());
        log.info("Encrypt base64: {}", CodecUtil.encodeToBase64(encrypt));
        log.info("Decrypt string: {}", CryptoUtil.decryptToString(encrypt));
    }

    @Test
    public void test3() {

        log.info("Digest base64: {}", CryptoUtil.digestToBase64(data));
    }

}
