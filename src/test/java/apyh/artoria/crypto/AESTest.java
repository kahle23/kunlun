package apyh.artoria.crypto;

import org.junit.Test;

// ECB (Electronic Code Book)   false
// CBC (Cipher Block Chaining)  true
// CFB (Cipher FeedBack)        true
// OFB (Output FeedBack)        true
// CTR (Counter)                true

// nopadding

public class AESTest {

    @Test
    public void test1() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK93L3HKK7Q5LLD42ZC".getBytes();
        byte[] iv = "XWQ6WCMAQAGFY0BR".getBytes();
        String data = "你好，Java！";
    }

}

// AES data Multiple 16
// AES Key length only is 16 or 24 or 32
// AES Iv length 16
// SecretKeySpec key = new SecretKeySpec(inputKey, "AES");
// new IvParameterSpec(iv);
// JDK
// "AES/ECB/NoPadding"
// "AES/ECB/PKCS5Padding"
// "AES/CBC/NoPadding"
// "AES/CBC/PKCS5Padding"
