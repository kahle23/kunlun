package artoriatest.crypto;

import org.junit.Test;
import artoria.crypto.Hmac;

public class HmacTest {

    @Test
    public void test1() throws Exception {
        String key = "12345";
        String data = "12345";
        System.out.println(Hmac.md5(key).calcToHex(data));
        System.out.println(Hmac.sha1(key).calcToHex(data));
        System.out.println(Hmac.sha256(key).calcToHex(data));
        System.out.println(Hmac.sha384(key).calcToHex(data));
        System.out.println(Hmac.sha512(key).calcToHex(data));
    }

}
