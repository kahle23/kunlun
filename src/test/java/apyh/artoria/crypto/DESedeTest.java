package apyh.artoria.crypto;

import apyh.artoria.codec.Base64;
import apyh.artoria.util.RandomUtils;
import org.junit.Test;

public class DESedeTest {

    @Test
    public void test1() throws Exception {
        byte[] key =  RandomUtils.nextString(24).getBytes();
        byte[] iv = RandomUtils.nextString(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        DESede desede = DESede.create(DESede.ECB_PKCS_5_PADDING, key);
        byte[] bytes = desede.encrypt("你好，Java！");
        System.out.println(Base64.encodeToString(bytes));
        System.out.println(new String(desede.decrypt(bytes)));
        DESede desede1 = DESede.create(DESede.CBC_PKCS_5_PADDING, key).setIv(iv);
        byte[] bytes1 = desede1.encrypt("你好，Java！");
        System.out.println(Base64.encodeToString(bytes1));
        System.out.println(new String(desede1.decrypt(bytes1)));
    }

    @Test
    public void test2() throws Exception {
        byte[] key = RandomUtils.nextString(24).getBytes();
        byte[] iv = RandomUtils.nextString(8).getBytes();
        System.out.println("key = " + new String(key));
        System.out.println("iv = " + new String(iv));
        DESede desede = DESede.create(DESede.ECB_NO_PADDING, key);
        byte[] bytes = desede.encrypt("你好，Java！");
        System.out.println(Base64.encodeToString(bytes));
        System.out.println(new String(desede.decrypt(bytes)));
        DESede desede1 = DESede.create(DESede.CBC_NO_PADDING, key).setIv(iv);
        byte[] bytes1 = desede1.encrypt("你好，Java！");
        System.out.println(Base64.encodeToString(bytes1));
        System.out.println(new String(desede1.decrypt(bytes1)));
    }

}
