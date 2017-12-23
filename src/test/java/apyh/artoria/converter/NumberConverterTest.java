package apyh.artoria.converter;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberConverterTest {

    private Converter converter = new NumberConverter();

    @Test
    public void test1() {
        int src = 100;
        Object o = converter.convert(src, Integer.class);
        System.out.println(o);
    }

    @Test
    public void test2() {
        int src = 100;
        Object o = converter.convert(src, BigDecimal.class);
        System.out.println(o);
    }

    @Test
    public void test3() {
        int src = 100;
        Object o = converter.convert(src, BigInteger.class);
        System.out.println(o);
    }

    @Test
    public void test4() {
        int src = 100;
        Object o = converter.convert(src, double.class);
        Double d = (Double) o;
        System.out.println(d);
    }

}
