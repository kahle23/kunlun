package apyh.artoria.crypto;

import apyh.artoria.codec.Base64;
import apyh.artoria.util.RandomUtils;
import org.junit.Test;

public class DESTest {

    @Test
    public void test1() throws Exception {
        byte[] key = RandomUtils.nextString(13).getBytes();
        byte[] iv = RandomUtils.nextString(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        DES des = DES.create(DES.ECB_PKCS_5_PADDING, key);
        byte[] bytes = des.encrypt("你好，Java！");
        System.out.println(Base64.encodeToString(bytes));
        System.out.println(new String(des.decrypt(bytes)));
        DES des1 = DES.create(DES.CBC_PKCS_5_PADDING, key).setIv(iv);
        byte[] bytes1 = des1.encrypt("你好，Java！");
        System.out.println(Base64.encodeToString(bytes1));
        System.out.println(new String(des1.decrypt(bytes1)));
    }

    @Test
    public void test2() throws Exception {
        byte[] key = RandomUtils.nextString(69).getBytes();
        byte[] iv = RandomUtils.nextString(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        DES des = DES.create(DES.ECB_NO_PADDING, key);
        byte[] bytes = des.encrypt("你好，Java！123");
        System.out.println(Base64.encodeToString(bytes));
        System.out.println(new String(des.decrypt(bytes)));
        DES des1 = DES.create(DES.CBC_NO_PADDING, key).setIv(iv);
        byte[] bytes1 = des1.encrypt("你好，Java！123");
        System.out.println(Base64.encodeToString(bytes1));
        System.out.println(new String(des1.decrypt(bytes1)));
    }

}
