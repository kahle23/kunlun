package com.apyhs.artoria.crypto;

import org.junit.Test;

public class DESTest {

    @Test
    public void test1() throws Exception {
        byte[] key = "TAB2QNW4UKPHY".getBytes();
        byte[] iv = "WLBSQ8CG".getBytes();
        String data = "你好，Java！";
    }

}

// DES data Multiple 8
// DES Key length >= 8
// DES Iv length 8
// SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(inputKey));
// new IvParameterSpec(iv);
// JDK
// "DES/ECB/NoPadding"
// "DES/ECB/PKCS5Padding"
// "DES/CBC/NoPadding"
// "DES/CBC/PKCS5Padding"
