package sabertest.codec;

import org.junit.Test;
import saber.codec.Base64;

public class Base64Test {

    @Test
    public void test1() throws Exception {
        String s1 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw==";
        String s2 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw==";
        String s3 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw=";
        String s4 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw=";
        String s5 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw";
        String s6 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw";
        System.out.println(Base64.isBase64UrlUnsafe(s1));
        System.out.println(Base64.isBase64UrlSafe(s2));
        System.out.println(Base64.isBase64UrlUnsafe(s3));
        System.out.println(Base64.isBase64UrlSafe(s4));
        System.out.println(Base64.isBase64UrlUnsafe(s5));
        System.out.println(Base64.isBase64UrlSafe(s6));
    }

}
