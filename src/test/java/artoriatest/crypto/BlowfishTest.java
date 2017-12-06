package artoriatest.crypto;

import artoria.codec.Base64;
import artoria.crypto.Blowfish;
import artoria.util.RandomUtils;
import org.junit.Test;

public class BlowfishTest {

    @Test
    public void test1() throws Exception {
        byte[] key = RandomUtils.nextString(56).getBytes();
        byte[] iv = RandomUtils.nextString(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        Blowfish blowfish = Blowfish.on(Blowfish.ECB_PKCS_5_PADDING, key);
        byte[] bytes = blowfish.encrypt("你好，Java！");
        System.out.println(Base64.encodeToString(bytes));
        System.out.println(new String(blowfish.decrypt(bytes)));
        Blowfish blowfish1 = Blowfish.on(Blowfish.CBC_PKCS_5_PADDING, key).setIv(iv);
        byte[] bytes1 = blowfish1.encrypt("你好，Java！");
        System.out.println(Base64.encodeToString(bytes1));
        System.out.println(new String(blowfish1.decrypt(bytes1)));
    }

    @Test
    public void test2() throws Exception {
        byte[] key = RandomUtils.nextString(19).getBytes();
        byte[] iv = RandomUtils.nextString(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        Blowfish blowfish = Blowfish.on(Blowfish.ECB_NO_PADDING, key);
        byte[] bytes = blowfish.encrypt("你好，Java！1");
        System.out.println(Base64.encodeToString(bytes));
        System.out.println(new String(blowfish.decrypt(bytes)));
        Blowfish blowfish1 = Blowfish.on(Blowfish.CBC_NO_PADDING, key).setIv(iv);
        byte[] bytes1 = blowfish1.encrypt("你好，Java！1");
        System.out.println(Base64.encodeToString(bytes1));
        System.out.println(new String(blowfish1.decrypt(bytes1)));
    }

}
