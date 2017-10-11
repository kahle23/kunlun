package sabertest.util.crypto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import saber.util.codec.Base64Utils;
import saber.util.crypto.BlowfishUtils;

public class BlowfishUtilsTest {

    @Test
    public void test1() throws Exception {
        byte[] key = RandomStringUtils.randomAscii(56).getBytes();
        byte[] iv = RandomStringUtils.randomAscii(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        byte[] bytes = BlowfishUtils.encryptEcbPkcs5Padding("你好，Java！".getBytes(), key);
        System.out.println(Base64Utils.encodeToString(bytes));
        System.out.println(new String(BlowfishUtils.decryptEcbPkcs5Padding(bytes, key)));
        byte[] bytes1 = BlowfishUtils.encryptCbcPkcs5Padding("你好，Java！".getBytes(), key, iv);
        System.out.println(Base64Utils.encodeToString(bytes1));
        System.out.println(new String(BlowfishUtils.decryptCbcPkcs5Padding(bytes1, key, iv)));
    }

    @Test
    public void test2() throws Exception {
        byte[] key = RandomStringUtils.randomAscii(19).getBytes();
        byte[] iv = RandomStringUtils.randomAscii(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        byte[] bytes = BlowfishUtils.encryptEcbNoPadding("你好，Java！1".getBytes(), key);
        System.out.println(Base64Utils.encodeToString(bytes));
        System.out.println(new String(BlowfishUtils.decryptEcbNoPadding(bytes, key)));
        byte[] bytes1 = BlowfishUtils.encryptCbcNoPadding("你好，Java！1".getBytes(), key, iv);
        System.out.println(Base64Utils.encodeToString(bytes1));
        System.out.println(new String(BlowfishUtils.decryptCbcNoPadding(bytes1, key, iv)));
    }

}
