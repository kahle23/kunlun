package artoria.crypto;

import artoria.codec.CodecUtils;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import static artoria.codec.CodecUtils.BASE64;
import static artoria.common.Constants.DES;

/**
 * DES/ECB/ZeroPadding(NoPadding)
 * DES/ECB/PKCS5Padding
 * DES/CBC/ZeroPadding(NoPadding)
 * DES/CBC/PKCS5Padding
 */
public class DESTest extends BouncyCastleSupport {
    private static Logger log = LoggerFactory.getLogger(DESTest.class);
    private static SymmetricCrypto symmetricCrypto = new SimpleSymmetricCrypto();
    private static IvParameterSpec ivParameterSpec;
    private byte[] data = "Hello, Java!".getBytes();

    static {
        try {
            // Wrong keySize: must be equal to 56
            String algorithm = DES; int keySize = 56;
            SecretKey secretKey = KeyUtils.generateKey(algorithm, keySize);
            log.info("Algorithm = {}, KeySize = {}", algorithm, keySize);
            log.info("Secret key: {}", KeyUtils.toBase64String(secretKey));
            // Wrong IV length: must be 8 bytes long
            ivParameterSpec = new IvParameterSpec("TeTestIv".getBytes());
            symmetricCrypto.setSecretKey(secretKey);
            symmetricCrypto.setAlgorithm(algorithm);
            symmetricCrypto.setMode(Mode.ECB);
            symmetricCrypto.setPadding(Padding.NO_PADDING);
        }
        catch (GeneralSecurityException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private void testEncryptAndDecrypt(Mode mode, Padding padding) throws Exception {
        log.info("Start test DES/{}/{}", mode, padding);
        symmetricCrypto.setMode(mode);
        symmetricCrypto.setPadding(padding);
        byte[] bytes = symmetricCrypto.encrypt(data);
        log.info("Encrypt: {}", CodecUtils.encodeToString(BASE64, bytes));
        byte[] bytes1 = symmetricCrypto.decrypt(bytes);
        log.info("Decrypt: {}", new String(bytes1));
        log.info("End test DES/{}/{}", mode, padding);
    }

    @Test
    public void testEcbZeroPadding() throws Exception {

        this.testEncryptAndDecrypt(Mode.ECB, Padding.ZERO_PADDING);
    }

    @Test
    public void testEcbPKCS5Padding() throws Exception {

        this.testEncryptAndDecrypt(Mode.ECB, Padding.PKCS5_PADDING);
    }

    @Test
    public void testCbcZeroPadding() throws Exception {
        symmetricCrypto.setAlgorithmParameterSpec(ivParameterSpec);
        this.testEncryptAndDecrypt(Mode.CBC, Padding.ZERO_PADDING);
        symmetricCrypto.setAlgorithmParameterSpec(null);
    }

    @Test
    public void testCbcPKCS5Padding() throws Exception {
        symmetricCrypto.setAlgorithmParameterSpec(ivParameterSpec);
        this.testEncryptAndDecrypt(Mode.CBC, Padding.PKCS5_PADDING);
        symmetricCrypto.setAlgorithmParameterSpec(null);
    }

    /**
     * The length of the DES key only supports 8.
     * The DES key length of the arguments when using the KeyGenerator is only 56.
     * Error info:"Wrong keySize: must be equal to 56".
     * So if the key length is correct, the encryption process is fine.
     * So the following code is only a backup.
     * @see KeyUtils#parseSecretKey(String, byte[])
     */
    public static SecretKey parseSecretKey(String algorithm, byte[] key) throws GeneralSecurityException {
        if (DES.equalsIgnoreCase(algorithm)) {
            KeySpec keySpec = new DESKeySpec(key);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            return factory.generateSecret(keySpec);
        }
        else {
            return new SecretKeySpec(key, algorithm);
        }
    }

}
