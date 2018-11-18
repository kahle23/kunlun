package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class Base64UtilsTest {
    private static Logger log = LoggerFactory.getLogger(Base64UtilsTest.class);

    @Test
    public void test1() {
        String s1 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw==";
        String s2 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw==";
        String s3 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw=";
        String s4 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw=";
        String s5 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw";
        String s6 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw";
        log.info("" + Base64Utils.isUrlUnsafeString(s1));
        log.info("" + Base64Utils.isUrlSafeString(s2));
        log.info("" + Base64Utils.isUrlUnsafeString(s3));
        log.info("" + Base64Utils.isUrlSafeString(s4));
        log.info("" + Base64Utils.isUrlUnsafeString(s5));
        log.info("" + Base64Utils.isUrlSafeString(s6));
    }

    @Test
    public void test2() {
        byte[] data = "Hello, World! �6n���ѿ�AS#".getBytes();
        log.info(Base64Utils.encodeToString(data));
        String urlSafeString = Base64Utils.encodeToUrlSafeString(data);
        log.info(urlSafeString);
        byte[] decode = Base64Utils.decodeFromUrlSafeString(urlSafeString);
        log.info(new String(decode));
    }

}
