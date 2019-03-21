package artoria.crypto;

import artoria.codec.HexUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Before;
import org.junit.Test;

public class HmacTest {
    private static Logger log = LoggerFactory.getLogger(HmacTest.class);
    private static Hmac hmd5 = Hmac.getInstance(Hmac.HMAC_MD5);
    private static Hmac hsha1 = Hmac.getInstance(Hmac.HMAC_SHA1);
    private static Hmac hsha256 = Hmac.getInstance(Hmac.HMAC_SHA256);
    private static Hmac hsha384 = Hmac.getInstance(Hmac.HMAC_SHA384);
    private static Hmac hsha512 = Hmac.getInstance(Hmac.HMAC_SHA512);

    @Before
    public void init() {
        String key = "123456";
        hmd5.setKey(key);
        hsha1.setKey(key);
        hsha256.setKey(key);
        hsha384.setKey(key);
        hsha512.setKey(key);
    }

    @Test
    public void hmacString() throws Exception {
        String data = "12345";
        log.info(HexUtils.encodeToString(hmd5.digest(data)));
        log.info(HexUtils.encodeToString(hsha1.digest(data)));
        log.info(HexUtils.encodeToString(hsha256.digest(data)));
        log.info(HexUtils.encodeToString(hsha384.digest(data)));
        log.info(HexUtils.encodeToString(hsha512.digest(data)));
    }

}
