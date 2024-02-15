package artoria.crypto;

import artoria.codec.CodecUtils;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static artoria.codec.CodecUtils.BASE64;
import static artoria.common.constant.Algorithms.RSA;
import static artoria.crypto.KeyType.PRIVATE_KEY;
import static artoria.crypto.KeyType.PUBLIC_KEY;

/**
 * RSA/None/NoPadding
 * RSA/None/PKCS1Padding
 * RSA/ECB/NoPadding
 * RSA/ECB/PKCS1Padding
 */
public class RSATest extends BouncyCastleSupport {
    private static Logger log = LoggerFactory.getLogger(RSATest.class);
    private static AsymmetricCrypto asymmetricCrypto = new SimpleAsymmetricCrypto();
    private byte[] data = "Hello, Java!".getBytes();

    static {
        try {
            // RSA keys must be at least 512 bits long
            String algorithm = RSA; int keySize = 2048;
            KeyPair keyPair = KeyUtils.generateKeyPair(algorithm, keySize);
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            log.info("Algorithm = {}, KeySize = {}", algorithm, keySize);
            log.info("Public key: {}", KeyUtils.toBase64String(publicKey));
            log.info("Private key: {}", KeyUtils.toBase64String(privateKey));
            asymmetricCrypto.setAlgorithm(RSA);
            asymmetricCrypto.setMode(Mode.NONE);
            asymmetricCrypto.setPadding(Padding.NO_PADDING);
            asymmetricCrypto.setPublicKey(publicKey);
            asymmetricCrypto.setPrivateKey(privateKey);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private void testEncryptAndDecrypt(Mode mode, Padding padding) throws Exception {
        log.info("Start test RSA/{}/{}", mode, padding);
        asymmetricCrypto.setMode(mode);
        asymmetricCrypto.setPadding(padding);
        byte[] bytes = asymmetricCrypto.encrypt(data, PUBLIC_KEY);
        log.info("Encrypt public key: {}", CodecUtils.encodeToString(BASE64, bytes));
        byte[] bytes1 = asymmetricCrypto.decrypt(bytes, PRIVATE_KEY);
        log.info("Decrypt private key: {}", new String(bytes1));
//        bytes1 = asymmetricCrypto.decrypt(bytes, PUBLIC_KEY);
//        log.info("Decrypt public key: {}", new String(bytes1));
        bytes = asymmetricCrypto.encrypt(data, PRIVATE_KEY);
        log.info("Encrypt private key: {}", CodecUtils.encodeToString(BASE64, bytes));
        bytes1 = asymmetricCrypto.decrypt(bytes, PUBLIC_KEY);
        log.info("Decrypt public key: {}", new String(bytes1));
//        bytes1 = asymmetricCrypto.decrypt(bytes, PRIVATE_KEY);
//        log.info("Decrypt private key: {}", new String(bytes1));
        log.info("End test RSA/{}/{}", mode, padding);
    }

    @Test
    public void testNoneNoPadding() throws Exception {

        this.testEncryptAndDecrypt(Mode.NONE, Padding.NO_PADDING);
    }

    @Test
    public void testNonePKCS1Padding() throws Exception {

        this.testEncryptAndDecrypt(Mode.NONE, Padding.PKCS1_PADDING);
    }

    @Test
    public void testEcbNoPadding() throws Exception {

        this.testEncryptAndDecrypt(Mode.ECB, Padding.NO_PADDING);
    }

    @Test
    public void testEcbPKCS1Padding() throws Exception {

        this.testEncryptAndDecrypt(Mode.ECB, Padding.PKCS1_PADDING);
    }

}
