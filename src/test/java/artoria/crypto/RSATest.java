package artoria.crypto;

import artoria.codec.Base64Utils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

// RSA
// generatorKeySize 2048
// "RSA/None/NoPadding"
// "RSA/None/PKCS1Padding"
// "RSA/ECB/NoPadding"
// "RSA/ECB/PKCS1Padding"

public class RSATest {
    private static Logger log = LoggerFactory.getLogger(RSATest.class);
    private String algorithmName = "RSA";
    private byte[] data = "Hello，Java！".getBytes();

    @Test
    @Ignore
    public void noneNoPaddingAndDecrypterBySelf() throws Exception {
        String trsft = "RSA/None/NoPadding";
        KeyPair keyPair = CipherUtils.generateKeyPair(algorithmName, 2048);

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        byte[] publicKeyByteArray = publicKey.getEncoded();
        log.info(Base64Utils.encodeToString(publicKeyByteArray));
        byte[] privateKeyByteArray = privateKey.getEncoded();
        log.info(Base64Utils.encodeToString(privateKeyByteArray));

        publicKey = CipherUtils.parsePublicKey(algorithmName, publicKeyByteArray);
        privateKey = CipherUtils.parsePrivateKey(algorithmName, privateKeyByteArray);

        Cipher encrypterPublic = CipherUtils.getEncrypter(trsft, publicKey);
        Cipher decrypterPublic = CipherUtils.getDecrypter(trsft, publicKey);
        Cipher encrypterPrivate = CipherUtils.getEncrypter(trsft, privateKey);
        Cipher decrypterPrivate = CipherUtils.getDecrypter(trsft, privateKey);

        byte[] bytes = encrypterPublic.doFinal(data);
        log.info("Encrypter Public: {}", Base64Utils.encodeToString(bytes));
        byte[] bytes1 = decrypterPublic.doFinal(bytes);
        log.info("Decrypter Public: {}", new String(bytes1));
        bytes1 = decrypterPrivate.doFinal(bytes);
        log.info("Decrypter Private: {}", new String(bytes1));

        log.info("");
        bytes = encrypterPrivate.doFinal(data);
        log.info("Encrypter Private: {}", Base64Utils.encodeToString(bytes));
        bytes1 = decrypterPrivate.doFinal(bytes);
        log.info("Decrypter Private: {}", new String(bytes1));
        bytes1 = decrypterPublic.doFinal(bytes);
        log.info("Decrypter Public: {}", new String(bytes1));
    }

    @Test
    @Ignore
    public void nonePKCS1Padding() throws Exception {
        String trsft = "RSA/None/PKCS1Padding";
        KeyPair keyPair = CipherUtils.generateKeyPair(algorithmName, 2048);

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        byte[] publicKeyByteArray = publicKey.getEncoded();
        log.info(Base64Utils.encodeToString(publicKeyByteArray));
        byte[] privateKeyByteArray = privateKey.getEncoded();
        log.info(Base64Utils.encodeToString(privateKeyByteArray));

        publicKey = CipherUtils.parsePublicKey(algorithmName, publicKeyByteArray);
        privateKey = CipherUtils.parsePrivateKey(algorithmName, privateKeyByteArray);

        Cipher encrypterPublic = CipherUtils.getEncrypter(trsft, publicKey);
        Cipher decrypterPublic = CipherUtils.getDecrypter(trsft, publicKey);
        Cipher encrypterPrivate = CipherUtils.getEncrypter(trsft, privateKey);
        Cipher decrypterPrivate = CipherUtils.getDecrypter(trsft, privateKey);

        byte[] bytes = encrypterPublic.doFinal(data);
        log.info("Encrypter Public: {}", Base64Utils.encodeToString(bytes));
        byte[] bytes1 = decrypterPrivate.doFinal(bytes);
        log.info("Decrypter Private: {}", new String(bytes1));

        log.info("");
        bytes = encrypterPrivate.doFinal(data);
        log.info("Encrypter Private: {}", Base64Utils.encodeToString(bytes));
        bytes1 = decrypterPublic.doFinal(bytes);
        log.info("Decrypter Public: {}", new String(bytes1));
    }

    @Test
    public void ecbNoPadding() throws Exception {
        String trsft = "RSA/ECB/NoPadding";
        KeyPair keyPair = CipherUtils.generateKeyPair(algorithmName, 2048);

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        byte[] publicKeyByteArray = publicKey.getEncoded();
        log.info(Base64Utils.encodeToString(publicKeyByteArray));
        byte[] privateKeyByteArray = privateKey.getEncoded();
        log.info(Base64Utils.encodeToString(privateKeyByteArray));

        publicKey = CipherUtils.parsePublicKey(algorithmName, publicKeyByteArray);
        privateKey = CipherUtils.parsePrivateKey(algorithmName, privateKeyByteArray);

        Cipher encrypterPublic = CipherUtils.getEncrypter(trsft, publicKey);
        Cipher decrypterPublic = CipherUtils.getDecrypter(trsft, publicKey);
        Cipher encrypterPrivate = CipherUtils.getEncrypter(trsft, privateKey);
        Cipher decrypterPrivate = CipherUtils.getDecrypter(trsft, privateKey);

        byte[] bytes = encrypterPublic.doFinal(data);
        log.info("Encrypter Public: {}", Base64Utils.encodeToString(bytes));
        byte[] bytes1 = decrypterPrivate.doFinal(bytes);
        log.info("Decrypter Private: {}", new String(bytes1));

        log.info("");
        bytes = encrypterPrivate.doFinal(data);
        log.info("Encrypter Private: {}", Base64Utils.encodeToString(bytes));
        bytes1 = decrypterPublic.doFinal(bytes);
        log.info("Decrypter Public: {}", new String(bytes1));
    }

    @Test
    public void ecbPKCS1Padding() throws Exception {
        String trsft = "RSA/ECB/PKCS1Padding";
        KeyPair keyPair = CipherUtils.generateKeyPair(algorithmName, 2048);

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        byte[] publicKeyByteArray = publicKey.getEncoded();
        log.info(Base64Utils.encodeToString(publicKeyByteArray));
        byte[] privateKeyByteArray = privateKey.getEncoded();
        log.info(Base64Utils.encodeToString(privateKeyByteArray));

        publicKey = CipherUtils.parsePublicKey(algorithmName, publicKeyByteArray);
        privateKey = CipherUtils.parsePrivateKey(algorithmName, privateKeyByteArray);

        Cipher encrypterPublic = CipherUtils.getEncrypter(trsft, publicKey);
        Cipher decrypterPublic = CipherUtils.getDecrypter(trsft, publicKey);
        Cipher encrypterPrivate = CipherUtils.getEncrypter(trsft, privateKey);
        Cipher decrypterPrivate = CipherUtils.getDecrypter(trsft, privateKey);

        byte[] bytes = encrypterPublic.doFinal(data);
        log.info("Encrypter Public: {}", Base64Utils.encodeToString(bytes));
        byte[] bytes1 = decrypterPrivate.doFinal(bytes);
        log.info("Decrypter Private: {}", new String(bytes1));

        log.info("");
        bytes = encrypterPrivate.doFinal(data);
        log.info("Encrypter Private: {}", Base64Utils.encodeToString(bytes));
        bytes1 = decrypterPublic.doFinal(bytes);
        log.info("Decrypter Public: {}", new String(bytes1));
    }

}
