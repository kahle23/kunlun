package artoria.crypto;

import artoria.codec.CodecUtils;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;

import static artoria.codec.CodecUtils.BASE64;
import static artoria.common.Constants.AES;

/**
 * AES/ECB/ZeroPadding(NoPadding)
 * AES/ECB/PKCS5Padding
 * AES/CBC/ZeroPadding(NoPadding)
 * AES/CBC/PKCS5Padding
 */
public class AESTest extends BouncyCastleSupport {
    private static Logger log = LoggerFactory.getLogger(AESTest.class);
    private static SymmetricCrypto symmetricCrypto = new SimpleSymmetricCrypto();
    private static IvParameterSpec ivParameterSpec;
    private byte[] data = "Hello, Java!".getBytes();

    static {
        try {
            // Wrong keySize: must be equal to 128, 192 or 256
            String algorithm = AES; int keySize = 128;
            SecretKey secretKey = KeyUtils.generateKey(algorithm, keySize);
            log.info("Algorithm = {}, KeySize = {}", algorithm, keySize);
            log.info("Secret key: {}", KeyUtils.toBase64String(secretKey));
            // Wrong IV length: must be 16 bytes long
            ivParameterSpec = new IvParameterSpec("TeTestIvTeTestIv".getBytes());
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
        log.info("Start test AES/{}/{}", mode, padding);
        symmetricCrypto.setMode(mode);
        symmetricCrypto.setPadding(padding);
        byte[] bytes = symmetricCrypto.encrypt(data);
        log.info("Encrypt: {}", CodecUtils.encodeToString(BASE64, bytes));
        byte[] bytes1 = symmetricCrypto.decrypt(bytes);
        log.info("Decrypt: {}", new String(bytes1));
        log.info("End test AES/{}/{}", mode, padding);
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

}
