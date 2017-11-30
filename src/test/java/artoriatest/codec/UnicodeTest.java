package artoriatest.codec;

import org.junit.Test;
import artoria.codec.Unicode;

public class UnicodeTest {

    @Test
    public void test1() {
        String unicode = Unicode.encode("你好，Java！");
        System.out.println(unicode);
        System.out.println(Unicode.decode(unicode));
    }

}
