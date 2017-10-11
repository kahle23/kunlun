package sabertest.util.crypto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import saber.util.codec.Base64Utils;
import saber.util.crypto.AesUtils;

public class AesUtilsTest {

    @Test
    public void test1() throws Exception {
        byte[] key = RandomStringUtils.randomAscii(32).getBytes();
        byte[] iv = RandomStringUtils.randomAscii(16).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        byte[] bytes = AesUtils.encryptEcbPkcs5Padding("你好，Java！".getBytes(), key);
        System.out.println(Base64Utils.encodeToString(bytes));
        System.out.println(new String(AesUtils.decryptEcbPkcs5Padding(bytes, key)));
        byte[] bytes1 = AesUtils.encryptCbcPkcs5Padding("你好，Java！".getBytes(), key, iv);
        System.out.println(Base64Utils.encodeToString(bytes1));
        System.out.println(new String(AesUtils.decryptCbcPkcs5Padding(bytes1, key, iv)));
    }

    @Test
    public void test2() throws Exception {
        byte[] key = RandomStringUtils.randomAscii(32).getBytes();
        byte[] iv = RandomStringUtils.randomAscii(16).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        byte[] bytes = AesUtils.encryptEcbNoPadding("你好，Java！1".getBytes(), key);
        System.out.println(Base64Utils.encodeToString(bytes));
        System.out.println(new String(AesUtils.decryptEcbNoPadding(bytes, key)));
        byte[] bytes1 = AesUtils.encryptCbcNoPadding("你好，Java！1".getBytes(), key, iv);
        System.out.println(Base64Utils.encodeToString(bytes1));
        System.out.println(new String(AesUtils.decryptCbcNoPadding(bytes1, key, iv)));
    }

}
