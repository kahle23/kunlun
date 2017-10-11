package saber.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;

public abstract class EncodeUtils {

    public static String recode(String data, String newEncode)
            throws IOException {
        Charset newer = Charset.forName(newEncode);
        return recode(data, newer);
    }

    public static String recode(String data, Charset newEncode)
            throws IOException {
        StringReader reader = new StringReader(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bos, newEncode);
        IOUtils.copy(reader, writer);
        writer.flush();
        return new String(bos.toByteArray(), newEncode);
    }

    public static byte[] recode(byte[] data, String oldEncode, String newEncode)
            throws IOException {
        Charset older = Charset.forName(oldEncode);
        Charset newer = Charset.forName(newEncode);
        return recode(data, older, newer);
    }

    public static byte[] recode(byte[] data, Charset oldEncode, Charset newEncode)
            throws IOException {
        StringReader reader = new StringReader(new String(data, oldEncode));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bos, newEncode);
        IOUtils.copy(reader, writer);
        writer.flush();
        return bos.toByteArray();
    }

    public static String encode(String data, String oldEncode, String newEncode) {
        Charset older = Charset.forName(oldEncode);
        Charset newer = Charset.forName(newEncode);
        return encode(data, older, newer);
    }

    public static String encode(String data, Charset oldEncode, Charset newEncode) {
        return new String(data.getBytes(oldEncode), newEncode);
    }

}
