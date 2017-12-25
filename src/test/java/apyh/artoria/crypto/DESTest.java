package apyh.artoria.crypto;

import org.junit.Test;

public class DESTest {

    @Test
    public void test1() throws Exception {
        byte[] key = "TAB2QNW4UKPHY".getBytes();
        byte[] iv = "WLBSQ8CG".getBytes();
        String data = "你好，Java！";
        CipherTestUtils.doTest(DES.ECB_PKCS_5_PADDING, key, iv, data);
        CipherTestUtils.doTest(DES.CBC_PKCS_5_PADDING, key, iv, data);
        CipherTestUtils.doTest(DES.ECB_NO_PADDING, key, iv, data);
        CipherTestUtils.doTest(DES.CBC_NO_PADDING, key, iv, data);
    }

}
