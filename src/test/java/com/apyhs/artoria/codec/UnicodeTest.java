package com.apyhs.artoria.codec;

import org.junit.Test;

public class UnicodeTest {

    @Test
    public void test1() {
        String unicode = Unicode.encode("Hello，Java！");
        System.out.println(unicode);
        System.out.println(Unicode.decode(unicode));
    }

    @Test
    public void test2() {
        StringBuilder builder = new StringBuilder();
        StringBuilder data = new StringBuilder("\\u003ctable cellpadding=0 cellspacing=0 class=\\u0027tableTitle\\u0027\\u003e\\u003ctr\\u003e\\u003ctd style=" +
                "\\u0027text-align:center;font-size:14px;font-weight:bold;color:#003399;\\u0027\\u003e\\u003cspan id=\\u0027HistoryName\\u0027\\u003e");
        while (data.length() > 0) {
            int i = data.indexOf("\\u00");
            if (i == -1) { continue; }
            if (i != 0) {
                builder.append(data.substring(0, i));
                data.delete(0, i);
            }
            if (data.length() > 6) {
                String substring = data.substring(0, 6);
                data.delete(0, 6);
                builder.append(Unicode.decode(substring));
            }
            else {
                builder.append(Unicode.decode(data.toString()));
                data.delete(0, data.length());
            }
        }
        System.out.println(builder);
    }

}
