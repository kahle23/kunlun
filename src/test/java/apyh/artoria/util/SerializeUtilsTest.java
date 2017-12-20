package apyh.artoria.util;

import apyh.artoria.codec.Hex;
import org.junit.Test;

import java.io.Serializable;

public class SerializeUtilsTest implements Serializable {

    @Test
    public void test() {
        SerializeUtilsTest obj = new SerializeUtilsTest();
        System.out.println(obj);
        byte[] bytes = SerializeUtils.serialize(obj);
        System.out.println(bytes.length);
        String encode = Hex.ME.encodeToString(bytes);
        System.out.println(encode);

        SerializeUtilsTest obj1 = (SerializeUtilsTest) SerializeUtils.deserialize(bytes);
        System.out.println(obj1);
    }

}
