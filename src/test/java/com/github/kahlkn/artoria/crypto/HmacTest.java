package com.github.kahlkn.artoria.crypto;

import org.junit.Test;

public class HmacTest {
    private static final String KEY = "123456";
    private static final Hmac HMD5 = Hmac.create(Hmac.HMAC_MD5).setKey(KEY);
    private static final Hmac HSHA1 = Hmac.create(Hmac.HMAC_SHA1).setKey(KEY);
    private static final Hmac HSHA256 = Hmac.create(Hmac.HMAC_SHA256).setKey(KEY);
    private static final Hmac HSHA384 = Hmac.create(Hmac.HMAC_SHA384).setKey(KEY);
    private static final Hmac HSHA512 = Hmac.create(Hmac.HMAC_SHA512).setKey(KEY);

    @Test
    public void hmacString() throws Exception {
        String data = "12345";
        System.out.println(HMD5.calcToHexString(data));
        System.out.println(HSHA1.calcToHexString(data));
        System.out.println(HSHA256.calcToHexString(data));
        System.out.println(HSHA384.calcToHexString(data));
        System.out.println(HSHA512.calcToHexString(data));
    }

}
