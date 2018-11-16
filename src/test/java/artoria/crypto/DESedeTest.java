package artoria.crypto;

import artoria.codec.Base64Utils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

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

public class DESedeTest {
    private static Logger log = LoggerFactory.getLogger(DESedeTest.class);
    private String algorithmName = "DESede";
    private byte[] data = "Hello，Java！".getBytes();

    @Test
    public void ecbNoPadding() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        String trsft = "DESede/ECB/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 8));
        log.info(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        log.info(new String(bytes1));
    }

    @Test
    public void ecbPKCS5Padding() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        String trsft = "DESede/ECB/PKCS5Padding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey);
        byte[] bytes = encrypter.doFinal(data);
        log.info(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey);
        byte[] bytes1 = decrypter.doFinal(bytes);
        log.info(new String(bytes1));
    }

    @Test
    public void cbcNoPadding() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        byte[] iv = "HESN0G1Q".getBytes();
        String trsft = "DESede/CBC/NoPadding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey, ivps);
        byte[] bytes = encrypter.doFinal(CipherUtils.fill(data, 8));
        log.info(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey, ivps);
        byte[] bytes1 = decrypter.doFinal(bytes);
        log.info(new String(bytes1));
    }

    @Test
    public void cbcPKCS5Padding() throws Exception {
        byte[] key = "XASA4BKBHXLTUAC3G8L76EQ0".getBytes();
        byte[] iv = "HESN0G1Q".getBytes();
        String trsft = "DESede/CBC/PKCS5Padding";
        SecretKey secretKey = CipherUtils.parseSecretKey(algorithmName, key);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        Cipher encrypter = CipherUtils.getEncrypter(trsft, secretKey, ivps);
        byte[] bytes = encrypter.doFinal(data);
        log.info(Base64Utils.encodeToString(bytes));

        Cipher decrypter = CipherUtils.getDecrypter(trsft, secretKey, ivps);
        byte[] bytes1 = decrypter.doFinal(bytes);
        log.info(new String(bytes1));
    }

}
