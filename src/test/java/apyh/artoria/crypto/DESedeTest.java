package apyh.artoria.crypto;

import org.junit.Test;

public class DESedeTest {

    @Test
    public void test1() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        byte[] iv = "HESN0G1Q".getBytes();
        String data = "你好，Java！";
    }

}

// DESede data Multiple 8
// DESede Key length 24
// DESede Iv length 8
// SecretKeySpec key = new SecretKeySpec(inputKey, "DESede");
// new IvParameterSpec(iv);
// JDK
// "DESede/ECB/NoPadding"
// "DESede/ECB/PKCS5Padding"
// "DESede/CBC/NoPadding"
// "DESede/CBC/PKCS5Padding"
