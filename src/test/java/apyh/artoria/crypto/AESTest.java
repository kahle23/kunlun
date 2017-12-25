package apyh.artoria.crypto;

import org.junit.Test;

public class AESTest {

    @Test
    public void test1() throws Exception {
        byte[] key = "IXUSMRSSFJQLHVK93L3HKK7Q5LLD42ZC".getBytes();
        byte[] iv = "XWQ6WCMAQAGFY0BR".getBytes();
        String data = "你好，Java！";
        CipherTestUtils.doTest(AES.ECB_PKCS_5_PADDING, key, iv, data);
        CipherTestUtils.doTest(AES.CBC_PKCS_5_PADDING, key, iv, data);
        CipherTestUtils.doTest(AES.ECB_NO_PADDING, key, iv, data);
        CipherTestUtils.doTest(AES.CBC_NO_PADDING, key, iv, data);
    }

}
