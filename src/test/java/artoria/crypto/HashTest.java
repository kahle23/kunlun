package artoria.crypto;

import artoria.codec.CodecUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import org.junit.Test;

import java.io.File;

import static artoria.codec.CodecUtils.HEX;
import static artoria.common.constant.Algorithms.*;

public class HashTest {
    private static final Logger log = LoggerFactory.getLogger(HashTest.class);
    private static final File testFile = new File("src\\test\\resources\\test_read.txt");
    private static final Hash md2 = new Hash(MD2);
    private static final Hash md5 = new Hash(MD5);
    private static final Hash sha1 = new Hash(SHA1);
    private static final Hash sha256 = new Hash(SHA256);
    private static final Hash sha384 = new Hash(SHA384);
    private static final Hash sha512 = new Hash(SHA512);

    @Test
    public void hashString() throws Exception {
        String data = "1234567890";
        log.info(CodecUtils.encodeToString(HEX, md2.digest(data)));
        log.info(CodecUtils.encodeToString(HEX, md5.digest(data)));
        log.info(CodecUtils.encodeToString(HEX, sha1.digest(data)));
        log.info(CodecUtils.encodeToString(HEX, sha256.digest(data)));
        log.info(CodecUtils.encodeToString(HEX, sha384.digest(data)));
        log.info(CodecUtils.encodeToString(HEX, sha512.digest(data)));
    }

    @Test
    public void hashFile() throws Exception {
        log.info("Please insure file is exists. ");
        Assert.isTrue(testFile.exists(), "File are not find. ");
        log.info(CodecUtils.encodeToString(HEX, md2.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, md5.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, sha1.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, sha256.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, sha384.digest(testFile)));
        log.info(CodecUtils.encodeToString(HEX, sha512.digest(testFile)));
    }

}
