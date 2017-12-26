package com.apyhs.artoria.crypto;

import org.junit.Test;

public class HmacTest {
    private static final String KEY = "123456";

    private static final Hmac hmd5 = Hmac.create(Hmac.HMAC_MD5).setKey(KEY);
    private static final Hmac hsha1 = Hmac.create(Hmac.HMAC_SHA1).setKey(KEY);
    private static final Hmac hsha256 = Hmac.create(Hmac.HMAC_SHA256).setKey(KEY);
    private static final Hmac hsha384 = Hmac.create(Hmac.HMAC_SHA384).setKey(KEY);
    private static final Hmac hsha512 = Hmac.create(Hmac.HMAC_SHA512).setKey(KEY);

    @Test
    public void test1() throws Exception {
        String data = "12345";
        System.out.println(hmd5.calcToHexString(data));
        System.out.println(hsha1.calcToHexString(data));
        System.out.println(hsha256.calcToHexString(data));
        System.out.println(hsha384.calcToHexString(data));
        System.out.println(hsha512.calcToHexString(data));
    }

}
