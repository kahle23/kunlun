package artoria.converter;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberConverterTest {
    private static Logger log = LoggerFactory.getLogger(NumberConverterTest.class);
    private TypeConverter converter = new NumberConverter();

    @Test
    public void test1() {
        int src = 100;
        Object o = converter.convert(src, Integer.class);
        log.info("" + o);
    }

    @Test
    public void test2() {
        int src = 100;
        Object o = converter.convert(src, BigDecimal.class);
        log.info("" + o);
    }

    @Test
    public void test3() {
        int src = 100;
        Object o = converter.convert(src, BigInteger.class);
        log.info("" + o);
    }

    @Test
    public void test4() {
        int src = 100;
        Object o = converter.convert(src, double.class);
        Double d = (Double) o;
        log.info("" + d);
    }

}
