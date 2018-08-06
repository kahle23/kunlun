package artoria.codec;

import org.junit.Test;

public class Base64UtilsTest {

    @Test
    public void test1() {
        String s1 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw==";
        String s2 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw==";
        String s3 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw=";
        String s4 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw=";
        String s5 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw";
        String s6 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw";
        System.out.println(Base64Utils.isUrlUnsafeString(s1));
        System.out.println(Base64Utils.isUrlSafeString(s2));
        System.out.println(Base64Utils.isUrlUnsafeString(s3));
        System.out.println(Base64Utils.isUrlSafeString(s4));
        System.out.println(Base64Utils.isUrlUnsafeString(s5));
        System.out.println(Base64Utils.isUrlSafeString(s6));
    }

    @Test
    public void test2() {
        byte[] data = "Hello, World! �6n���ѿ�AS#".getBytes();
        System.out.println(Base64Utils.encodeToString(data));
        String urlSafeString = Base64Utils.encodeToUrlSafeString(data);
        System.out.println(urlSafeString);
        byte[] decode = Base64Utils.decodeFromUrlSafeString(urlSafeString);
        System.out.println(new String(decode));
    }

}
