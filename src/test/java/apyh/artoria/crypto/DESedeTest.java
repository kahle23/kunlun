package apyh.artoria.crypto;

import org.junit.Test;

public class DESedeTest {

    @Test
    public void test1() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        byte[] iv = "HESN0G1Q".getBytes();
        String data = "你好，Java！";
        CipherTestUtils.doTest(DESede.ECB_PKCS_5_PADDING, key, iv, data);
        CipherTestUtils.doTest(DESede.CBC_PKCS_5_PADDING, key, iv, data);
        CipherTestUtils.doTest(DESede.ECB_NO_PADDING, key, iv, data);
        CipherTestUtils.doTest(DESede.CBC_NO_PADDING, key, iv, data);
    }

}
