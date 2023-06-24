package artoria.data.serialize.support;

import artoria.codec.CodecUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.Serializable;

import static artoria.codec.CodecUtils.HEX;

/**
 * The simple serializer Test.
 * @author Kahle
 */
public class SimpleSerializerTest implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(SimpleSerializerTest.class);
    private static final SimpleSerializer serializer = new SimpleSerializer();

    @Test
    public void testSerializeAndDeserialize() {
        SimpleSerializerTest obj = new SimpleSerializerTest();
        log.info("{}", obj);
        byte[] bytes = serializer.serialize(obj);
        String encode = CodecUtils.encodeToString(HEX, bytes);
        log.info(encode);

        SimpleSerializerTest obj1 = (SimpleSerializerTest) serializer.deserialize(bytes);
        log.info("{}", obj1);
    }

}
