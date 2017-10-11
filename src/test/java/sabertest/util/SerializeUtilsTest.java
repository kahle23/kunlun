package sabertest.util;

import org.junit.Test;
import saber.util.SerializeUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class SerializeUtilsTest implements Serializable {

    @Test
    public void test() throws IOException, ClassNotFoundException {
        SerializeUtilsTest obj = new SerializeUtilsTest();
        System.out.println(obj);
        byte[] bytes = SerializeUtils.serialize(obj);
        System.out.println(Arrays.toString(bytes));

        SerializeUtilsTest obj1 = (SerializeUtilsTest) SerializeUtils.deserialize(bytes);
        System.out.println(obj1);
    }

}
