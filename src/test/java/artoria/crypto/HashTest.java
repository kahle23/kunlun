package artoria.crypto;

import artoria.codec.HexUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import org.junit.Test;

import java.io.File;

import static artoria.common.Constants.*;

public class HashTest {
    private static Logger log = LoggerFactory.getLogger(HashTest.class);
    private static File testFile = new File("src\\test\\resources\\test_read.txt");
    private static Hash md2 = new Hash(MD2);
    private static Hash md5 = new Hash(MD5);
    private static Hash sha1 = new Hash(SHA1);
    private static Hash sha256 = new Hash(SHA256);
    private static Hash sha384 = new Hash(SHA384);
    private static Hash sha512 = new Hash(SHA512);

    @Test
    public void hashString() throws Exception {
        String data = "1234567890";
        log.info(HexUtils.encodeToString(md2.digest(data)));
        log.info(HexUtils.encodeToString(md5.digest(data)));
        log.info(HexUtils.encodeToString(sha1.digest(data)));
        log.info(HexUtils.encodeToString(sha256.digest(data)));
        log.info(HexUtils.encodeToString(sha384.digest(data)));
        log.info(HexUtils.encodeToString(sha512.digest(data)));
    }

    @Test
    public void hashFile() throws Exception {
        log.info("Please insure file is exists. ");
        Assert.isTrue(testFile.exists(), "File are not find. ");
        log.info(HexUtils.encodeToString(md2.digest(testFile)));
        log.info(HexUtils.encodeToString(md5.digest(testFile)));
        log.info(HexUtils.encodeToString(sha1.digest(testFile)));
        log.info(HexUtils.encodeToString(sha256.digest(testFile)));
        log.info(HexUtils.encodeToString(sha384.digest(testFile)));
        log.info(HexUtils.encodeToString(sha512.digest(testFile)));
    }

}
