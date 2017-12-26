package com.apyhs.artoria.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;

/**
 * Change string show charset.
 * And recode string charset.
 * @author Kahle
 */
public class Encoder {

    public static String encode(String data, String oldCharset, String newCharset) {
        Assert.notBlank(oldCharset, "Old charset must is not blank. ");
        Assert.notBlank(newCharset, "New charset must is not blank. ");
        Charset older = Charset.forName(oldCharset);
        Charset newer = Charset.forName(newCharset);
        return Encoder.encode(data, older, newer);
    }

    public static String encode(String data, Charset oldCharset, Charset newCharset) {
        Assert.notNull(data, "Data must is not null. ");
        Assert.notNull(oldCharset, "Old charset must is not null. ");
        Assert.notNull(newCharset, "New charset must is not null. ");
        byte[] bytes = data.getBytes(oldCharset);
        return new String(bytes, newCharset);
    }

    public static String recode(String data, String newCharset) throws IOException {
        Assert.notBlank(newCharset, "New charset must is not blank. ");
        Charset newer = Charset.forName(newCharset);
        return Encoder.recode(data, newer);
    }

    public static String recode(String data, Charset newCharset) throws IOException {
        Assert.notNull(data, "Data must is not null. ");
        Assert.notNull(newCharset, "New charset must is not null. ");
        StringReader reader = new StringReader(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bos, newCharset);
        IOUtils.copyLarge(reader, writer);
        writer.flush();
        byte[] bytes = bos.toByteArray();
        return new String(bytes, newCharset);
    }

    public static byte[] recode(byte[] data, String oldCharset, String newCharset) throws IOException {
        Assert.notBlank(oldCharset, "Old charset must is not blank. ");
        Assert.notBlank(newCharset, "New charset must is not blank. ");
        Charset older = Charset.forName(oldCharset);
        Charset newer = Charset.forName(newCharset);
        return Encoder.recode(data, older, newer);
    }

    public static byte[] recode(byte[] data, Charset oldCharset, Charset newCharset) throws IOException {
        Assert.notNull(data, "Data must is not null. ");
        Assert.notNull(oldCharset, "Old charset must is not null. ");
        Assert.notNull(newCharset, "New charset must is not null. ");
        String dataStr = new String(data, oldCharset);
        StringReader reader = new StringReader(dataStr);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bos, newCharset);
        IOUtils.copyLarge(reader, writer);
        writer.flush();
        return bos.toByteArray();
    }

}
