package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class UnicodeTest {
    private static Logger log = LoggerFactory.getLogger(UnicodeTest.class);
    private static Unicode unicode = new Unicode();

    @Test
    public void test1() {
        String encode = unicode.encode("Hello，Java! ");
        log.info(encode);
        log.info(unicode.decode(encode));
    }

    @Test
    public void test2() {
        String data = "\\u003ctable cellpadding=0 cellspacing=0 class=\\u0027tableTitle\\u0027\\u003e\\u003ctr\\u003e\\u003ctd style=" +
                "\\u0027text-align:center;font-size:14px;font-weight:bold;color:#003399;\\u0027\\u003e\\u003cspan id=\\u0027HistoryName\\u0027\\u003e";
        log.info(unicode.decode(data));
    }

}
