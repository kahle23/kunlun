package artoria.crypto;

import artoria.codec.HexUtils;
import org.junit.Test;

public class HmacTest {
    private static final Hmac HMD5 = new Hmac(Hmac.HMAC_MD5);
    private static final Hmac HSHA1 = new Hmac(Hmac.HMAC_SHA1);
    private static final Hmac HSHA256 = new Hmac(Hmac.HMAC_SHA256);
    private static final Hmac HSHA384 = new Hmac(Hmac.HMAC_SHA384);
    private static final Hmac HSHA512 = new Hmac(Hmac.HMAC_SHA512);
    private static final String KEY = "123456";

    static {
        HMD5.setKey(KEY);
        HSHA1.setKey(KEY);
        HSHA256.setKey(KEY);
        HSHA384.setKey(KEY);
        HSHA512.setKey(KEY);
    }

    @Test
    public void hmacString() throws Exception {
        String data = "12345";
        System.out.println(HexUtils.encodeToString(HMD5.calc(data)));
        System.out.println(HexUtils.encodeToString(HSHA1.calc(data)));
        System.out.println(HexUtils.encodeToString(HSHA256.calc(data)));
        System.out.println(HexUtils.encodeToString(HSHA384.calc(data)));
        System.out.println(HexUtils.encodeToString(HSHA512.calc(data)));
    }

}
