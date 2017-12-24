package apyh.artoria.crypto;

import apyh.artoria.codec.Base64;
import org.junit.Test;

import java.security.GeneralSecurityException;

public class DESedeTest {
    private byte[] key;
    private byte[] iv;

    public void doTest(String transformation, String data)
            throws GeneralSecurityException {
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        Cipher desedeEncrypt = CipherFactory.getCipher(transformation);
        desedeEncrypt.setMode(Cipher.Mode.ENCRYPT);
        desedeEncrypt.setKey(key).setIv(iv);
        Cipher desedeDecrypt = CipherFactory.getCipher(transformation);
        desedeDecrypt.setMode(Cipher.Mode.DECRYPT);
        desedeDecrypt.setKey(key).setIv(iv);

        byte[] bytes = desedeEncrypt.calc(data);
        System.out.println(Base64.encodeToString(bytes));
        System.out.println(desedeDecrypt.calcToString(bytes));
    }

    @Test
    public void test1() throws Exception {
        key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        iv = "HESN0G1Q".getBytes();
        String data = "你好，Java！";
        doTest(DESede.ECB_PKCS_5_PADDING, data);
        doTest(DESede.CBC_PKCS_5_PADDING, data);
        doTest(DESede.ECB_NO_PADDING, data);
        doTest(DESede.CBC_NO_PADDING, data);
    }

}
