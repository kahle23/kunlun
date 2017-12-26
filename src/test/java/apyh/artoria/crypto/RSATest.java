package apyh.artoria.crypto;

public class RSATest {

    /*@Test
    public void test1() throws Exception {
        Cipher cipher = CipherFactory.getCipher(RSA.ECB_NO_PADDING);
        RSA rsaPublicKey = (RSA) cipher;
        Cipher cipher1 = CipherFactory.getCipher(RSA.ECB_NO_PADDING);
        RSA rsaPrivateKey = (RSA) cipher1;

        KeyPair keyPair = rsaPublicKey.setKeyPairGenerator(4096).generateKey();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String pubKey = Base64.encodeToString(publicKey.getEncoded());
        String priKey = Base64.encodeToString(privateKey.getEncoded());
        System.out.println("PublicKey : " + pubKey);
        System.out.println("PrivateKey : " + priKey);

        rsaPublicKey.setPublicKey(publicKey.getEncoded());
//        rsaPublicKey.setMode(Cipher.Mode.ENCRYPT);
        rsaPublicKey.setMode(Cipher.Mode.DECRYPT);
        rsaPrivateKey.setPrivateKey(privateKey.getEncoded());
//        rsaPrivateKey.setMode(Cipher.Mode.DECRYPT);
        rsaPrivateKey.setMode(Cipher.Mode.ENCRYPT);

//        String data = "Hello, Java! ";
//        byte[] encrypt = rsaPublicKey.encrypt(data.getBytes());
//        System.out.println("PublicKey加密结果：" + Base64.encodeToString(encrypt));
//        byte[] decrypt = rsaPrivateKey.decrypt(encrypt);
//        System.out.println("PrivateKey解密结果：" + new String(decrypt));
        String data = "Hello, Java! ";
        byte[] encrypt = rsaPrivateKey.calc(data.getBytes());
        System.out.println("PrivateKey加密结果：" + Base64.encodeToString(encrypt));
        byte[] decrypt = rsaPublicKey.calc(encrypt);
        System.out.println("PublicKey解密结果：" + new String(decrypt));

    }*/

//    @Test
//    public void test1() throws Exception {
//        RSA rsa = RSA.create().generateKey();
//        String pubKey = Base64.encodeBase64String(rsa.getPublicKey().getEncoded());
//        String priKey = Base64.encodeBase64String(rsa.getPrivateKey().getEncoded());
//        System.out.println(pubKey);
//        System.out.println(priKey);
//        RSA rsa1 = RSA.create().parsePublicKey(Base64.decodeBase64(pubKey))
//                .parsePrivateKey(Base64.decodeBase64(priKey));
//        String pubKey1 = Base64.encodeBase64String(rsa1.getPublicKey().getEncoded());
//        String priKey1 = Base64.encodeBase64String(rsa1.getPrivateKey().getEncoded());
//        System.out.println(pubKey1);
//        System.out.println(priKey1);
//        System.out.println(pubKey.equals(pubKey1));
//        System.out.println(priKey.equals(priKey1));
//    }
//
//    @Test
//    public void test2() throws Exception {
//        // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        String data = "Hello, World!111";
//        System.out.println(data);
//        RSA rsa = RSA.create().generateKey();
//        // rsa.setTransformation("RSA/None/NoPadding");
//        // rsa.setTransformation("RSA/None/PKCS1Padding");
//        // rsa.setTransformation("RSA/ECB/NoPadding");
//        // rsa.setTransformation("RSA/ECB/PKCS1Padding");
//        System.out.println("Transformation : " + rsa.getTransformation());
//        String encryptString = Base64.encodeBase64String(rsa.encrypt(data));
//        System.out.println(encryptString);
//
//        // rsa.setTransformation("RSA/None/PKCS1Padding");
//        // rsa.setTransformation("RSA/ECB/NoPadding"); // 前后填充方式不一致 可能导致解码错误
//        // rsa.setTransformation("RSA/ECB/PKCS5Padding"); // PKCS5Padding unavailable with RSA.
//        String decryptString = new String(rsa.decrypt(Base64.decodeBase64(encryptString)));
//        System.out.println(decryptString);
//    }
//
//    @Test
//    public void test3() throws Exception {
//        //  Data must not be longer than 245 bytes
//        String data = "Hello, World!Hello, World!Hello1, Worldell, World!Hello, Worlld!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!Hello, World!";
//        System.out.println(data.getBytes().length);
//        RSA rsa = RSA.create(RSA.ECB_PKCS_1_PADDING).generateKey();
//        String encrypt = Base64.encodeBase64String(rsa.encrypt(data));
//        System.out.println(encrypt);
//
//        System.out.println(new String(rsa.decrypt(Base64.decodeBase64(encrypt))));
//    }

}

// RSA
// generatorKeySize 2048
// "RSA/None/NoPadding"
// "RSA/None/PKCS1Padding"
// "RSA/ECB/NoPadding"
// "RSA/ECB/PKCS1Padding"
