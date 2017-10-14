package sabertest.crypto;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import saber.crypto.RSA;

public class RSATest {

    @Test
    public void test1() throws Exception {
        RSA rsa = RSA.on().generateKey();
        String pubKey = Base64.encodeBase64String(rsa.getPublicKey().getEncoded());
        String priKey = Base64.encodeBase64String(rsa.getPrivateKey().getEncoded());
        System.out.println(pubKey);
        System.out.println(priKey);
        RSA rsa1 = RSA.on().parsePublicKey(Base64.decodeBase64(pubKey))
                .parsePrivateKey(Base64.decodeBase64(priKey));
        String pubKey1 = Base64.encodeBase64String(rsa1.getPublicKey().getEncoded());
        String priKey1 = Base64.encodeBase64String(rsa1.getPrivateKey().getEncoded());
        System.out.println(pubKey1);
        System.out.println(priKey1);
        System.out.println(pubKey.equals(pubKey1));
        System.out.println(priKey.equals(priKey1));
    }

    @Test
    public void test2() throws Exception {
        // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        String data = "Hello, World! ";
        System.out.println(data);
        RSA rsa = RSA.on().generateKey();
        // rsa.setTransformation("RSA/None/PKCS1Padding");
        // rsa.setTransformation("RSA/ECB/PKCS1Padding");
        System.out.println("Transformation : " + rsa.getTransformation());
        String encryptString = Base64.encodeBase64String(rsa.encrypt(data.getBytes()));
        System.out.println(encryptString);

        // rsa.setTransformation("RSA/None/PKCS1Padding");
        // rsa.setTransformation("RSA/ECB/NoPadding"); // 前后填充方式不一致 可能导致解码错误
        // rsa.setTransformation("RSA/ECB/PKCS5Padding"); // PKCS5Padding unavailable with RSA.
        String decryptString = new String(rsa.decrypt(Base64.decodeBase64(encryptString)));
        System.out.println(decryptString);
    }


}
