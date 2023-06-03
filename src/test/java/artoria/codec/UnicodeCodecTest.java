package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.codec.CodecUtils.UNICODE;

/**
 * The unicode codec tools Test.
 * @author Kahle
 */
public class UnicodeCodecTest {
    private static final Logger log = LoggerFactory.getLogger(UnicodeCodecTest.class);

    @Test
    public void test1() {
        String encode = CodecUtils.encode(UNICODE, "Hello，Java! ");
        log.info(encode);
        log.info(CodecUtils.decode(UNICODE, encode));
    }

    @Test
    public void test2() {
        String data = "\\u003ctable cellpadding=0 cellspacing=0 class=\\u0027tableTitle\\u0027\\u003e\\u003ctr\\u003e\\u003ctd style=" +
                "\\u0027text-align:center;font-size:14px;font-weight:bold;color:#003399;\\u0027\\u003e\\u003cspan id=\\u0027HistoryName\\u0027\\u003e";
        log.info(CodecUtils.decode(UNICODE, data));
    }

}
