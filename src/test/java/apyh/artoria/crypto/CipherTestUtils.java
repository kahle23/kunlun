package apyh.artoria.crypto;

import apyh.artoria.codec.Base64;
import apyh.artoria.logging.Logger;
import apyh.artoria.logging.LoggerFactory;

import java.security.GeneralSecurityException;

import static apyh.artoria.util.StringConstant.ENDL;

public class CipherTestUtils {
    private static final Logger log = LoggerFactory.getLogger(CipherTestUtils.class);

    public static void doTest(String transformation, byte[] key, byte[] iv, String data)
            throws GeneralSecurityException {
        StringBuilder builder = new StringBuilder(ENDL);
        builder.append("----  ").append(transformation).append("  ----").append(ENDL);
        builder.append("----------------------------").append(ENDL);
        builder.append("key = ").append(new String(key)).append(ENDL);
        builder.append("iv = ").append(new String(iv)).append(ENDL);
        Cipher desedeEncrypt = CipherFactory.getCipher(transformation);
        desedeEncrypt.setMode(Cipher.Mode.ENCRYPT);
        desedeEncrypt.setKey(key).setIv(iv);
        Cipher desedeDecrypt = CipherFactory.getCipher(transformation);
        desedeDecrypt.setMode(Cipher.Mode.DECRYPT);
        desedeDecrypt.setKey(key).setIv(iv);

        byte[] bytes = desedeEncrypt.calc(data);
        builder.append("Encrypt : ").append(Base64.encodeToString(bytes)).append(ENDL);
        builder.append("Decrypt : ").append(desedeDecrypt.calcToString(bytes)).append(ENDL);
        builder.append("----------------------------").append(ENDL);
        log.info(builder.toString());
    }

}
