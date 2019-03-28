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
    public void testIntToInteger() {
        int src = 100;
        Object obj = converter.convert(src, Integer.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testIntToBigDecimal() {
        int src = 100;
        Object obj = converter.convert(src, BigDecimal.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testIntToBigInteger() {
        int src = 100;
        Object obj = converter.convert(src, BigInteger.class);
        log.info("{} {}", obj.getClass(), obj);
    }

    @Test
    public void testIntToPrimitiveDouble() {
        int src = 100;
        Object obj = converter.convert(src, double.class);
        log.info("{} {}", obj.getClass(), obj);
    }

}
