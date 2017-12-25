package apyh.artoria.crypto;

import org.junit.Test;

public class BlowfishTest {

    @Test
    public void test1() throws Exception {
        byte[] key = "YMHADK1XE12U1F925LZNJP3X21U5PIL5ZTOHU1B9CXOQ6449UTI3QQLA".getBytes();
        byte[] iv = "J3CPV1FL".getBytes();
        String data = "你好，Java！";
        CipherTestUtils.doTest(Blowfish.ECB_PKCS_5_PADDING, key, iv, data);
        CipherTestUtils.doTest(Blowfish.CBC_PKCS_5_PADDING, key, iv, data);
        CipherTestUtils.doTest(Blowfish.ECB_NO_PADDING, key, iv, data);
        CipherTestUtils.doTest(Blowfish.CBC_NO_PADDING, key, iv, data);
    }

}
