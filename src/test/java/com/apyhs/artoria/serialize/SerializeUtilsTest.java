package com.apyhs.artoria.serialize;

import com.apyhs.artoria.codec.Hex;
import org.junit.Test;

import java.io.Serializable;

public class SerializeUtilsTest implements Serializable {

    @Test
    public void serializeAndDeserialize() {
        SerializeUtilsTest obj = new SerializeUtilsTest();
        System.out.println(obj);
        byte[] bytes = SerializeUtils.serialize(obj);
        String encode = Hex.ME.encodeToString(bytes);
        System.out.println(encode);

        SerializeUtilsTest obj1 = (SerializeUtilsTest) SerializeUtils.deserialize(bytes);
        System.out.println(obj1);
    }

}
