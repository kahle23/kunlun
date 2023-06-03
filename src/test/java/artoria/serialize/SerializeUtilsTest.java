package artoria.serialize;

import artoria.codec.CodecUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.Serializable;

public class SerializeUtilsTest implements Serializable {
    private static Logger log = LoggerFactory.getLogger(SerializeUtilsTest.class);

    @Test
    public void serializeAndDeserialize() {
        SerializeUtilsTest obj = new SerializeUtilsTest();
        log.info("{}", obj);
        byte[] bytes = SerializeUtils.serialize(obj);
        String encode = CodecUtils.encodeToString(CodecUtils.HEX, bytes);
        log.info(encode);

        SerializeUtilsTest obj1 = (SerializeUtilsTest) SerializeUtils.deserialize(bytes);
        log.info("{}", obj1);
    }

}
