package sabertest.codec;

import org.junit.Test;
import saber.codec.UnicodeUtils;

public class UnicodeUtilsTest {

    @Test
    public void test1() {
        String unicode = UnicodeUtils.encode("你好，Java！");
        System.out.println(unicode);
        System.out.println(UnicodeUtils.decode(unicode));
    }

}
