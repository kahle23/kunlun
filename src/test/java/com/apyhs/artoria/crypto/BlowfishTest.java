package com.apyhs.artoria.crypto;

import org.junit.Test;

public class BlowfishTest {

    @Test
    public void test1() throws Exception {
        byte[] key = "YMHADK1XE12U1F925LZNJP3X21U5PIL5ZTOHU1B9CXOQ6449UTI3QQLA".getBytes();
        byte[] iv = "J3CPV1FL".getBytes();
        String data = "你好，Java！";
    }

}

// Blowfish data Multiple 8
// Blowfish Key length <= 56
// Blowfish Iv length 8
// SecretKeySpec key = new SecretKeySpec(inputKey, "Blowfish");
// new IvParameterSpec(iv);
// JDK
// "Blowfish/ECB/NoPadding"
// "Blowfish/ECB/PKCS5Padding"
// "Blowfish/CBC/NoPadding"
// "Blowfish/CBC/PKCS5Padding"
