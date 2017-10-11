package sabertest.crypto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import saber.codec.Base64Utils;
import saber.crypto.DesUtils;

public class DesUtilsTest {

    @Test
    public void test1() throws Exception {
        byte[] key = RandomStringUtils.randomAscii(13).getBytes();
        byte[] iv = RandomStringUtils.randomAscii(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        byte[] bytes = DesUtils.encryptEcbPkcs5Padding("你好，Java！".getBytes(), key);
        System.out.println(Base64Utils.encodeToString(bytes));
        System.out.println(new String(DesUtils.decryptEcbPkcs5Padding(bytes, key)));
        byte[] bytes1 = DesUtils.encryptCbcPkcs5Padding("你好，Java！".getBytes(), key, iv);
        System.out.println(Base64Utils.encodeToString(bytes1));
        System.out.println(new String(DesUtils.decryptCbcPkcs5Padding(bytes1, key, iv)));
    }

    @Test
    public void test2() throws Exception {
        byte[] key = RandomStringUtils.randomAscii(69).getBytes();
        byte[] iv = RandomStringUtils.randomAscii(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        byte[] bytes = DesUtils.encryptEcbNoPadding("你好，Java！".getBytes(), key);
        System.out.println(Base64Utils.encodeToString(bytes));
        System.out.println(new String(DesUtils.decryptEcbNoPadding(bytes, key)));
        byte[] bytes1 = DesUtils.encryptCbcNoPadding("你好，Java！".getBytes(), key, iv);
        System.out.println(Base64Utils.encodeToString(bytes1));
        System.out.println(new String(DesUtils.decryptCbcNoPadding(bytes1, key, iv)));
    }

}
