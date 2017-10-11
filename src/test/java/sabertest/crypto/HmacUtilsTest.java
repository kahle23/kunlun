package sabertest.crypto;

import org.junit.Test;
import saber.crypto.HmacUtils;

public class HmacUtilsTest {

    @Test
    public void test1() throws Exception {
        System.out.println(HmacUtils.hmd5("123456", "aaa"));
        System.out.println(HmacUtils.hsha1("123456", "aaa"));
        System.out.println(HmacUtils.hsha256("123456", "aaa"));
        System.out.println(HmacUtils.hsha384("123456", "aaa"));
        System.out.println(HmacUtils.hsha512("123456", "aaa"));
    }

}
