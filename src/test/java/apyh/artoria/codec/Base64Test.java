package apyh.artoria.codec;

import org.junit.Test;

public class Base64Test {

    @Test
    public void test1() throws Exception {
        String s1 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw==";
        String s2 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw==";
        String s3 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw=";
        String s4 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw=";
        String s5 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw";
        String s6 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw";
        System.out.println(Base64.isUrlUnsafeString(s1));
        System.out.println(Base64.isUrlSafeString(s2));
        System.out.println(Base64.isUrlUnsafeString(s3));
        System.out.println(Base64.isUrlSafeString(s4));
        System.out.println(Base64.isUrlUnsafeString(s5));
        System.out.println(Base64.isUrlSafeString(s6));
    }

    @Test
    public void test2() {
        byte[] data = "Hello, World! �6n���ѿ�AS#".getBytes();
        System.out.println(Base64.encodeToString(data));
        String urlSafeString = Base64.encodeToUrlSafeString(data);
        System.out.println(urlSafeString);
        byte[] decode = Base64.decodeFromUrlSafeString(urlSafeString);
        System.out.println(new String(decode));
    }

}
