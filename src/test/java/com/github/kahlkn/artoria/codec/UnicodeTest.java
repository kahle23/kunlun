package com.github.kahlkn.artoria.codec;

import org.junit.Test;

public class UnicodeTest {

    @Test
    public void test1() {
        String unicode = Unicode.encode("Hello，Java! ");
        System.out.println(unicode);
        System.out.println(Unicode.decode(unicode));
    }

    @Test
    public void test2() {
        String data = "\\u003ctable cellpadding=0 cellspacing=0 class=\\u0027tableTitle\\u0027\\u003e\\u003ctr\\u003e\\u003ctd style=" +
                "\\u0027text-align:center;font-size:14px;font-weight:bold;color:#003399;\\u0027\\u003e\\u003cspan id=\\u0027HistoryName\\u0027\\u003e";
        System.out.println(Unicode.decode(data));
    }

}
