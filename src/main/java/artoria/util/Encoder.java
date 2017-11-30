package artoria.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;

/**
 * @author Kahle
 */
public class Encoder {

    public static String recode(String data, String newCharset)
            throws IOException {
        Charset newer = Charset.forName(newCharset);
        return recode(data, newer);
    }

    public static String recode(String data, Charset newCharset)
            throws IOException {
        StringReader reader = new StringReader(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bos, newCharset);
        IOUtils.copy(reader, writer);
        writer.flush();
        return new String(bos.toByteArray(), newCharset);
    }

    public static byte[] recode(byte[] data, String oldCharset, String newCharset)
            throws IOException {
        Charset older = Charset.forName(oldCharset);
        Charset newer = Charset.forName(newCharset);
        return recode(data, older, newer);
    }

    public static byte[] recode(byte[] data, Charset oldCharset, Charset newCharset)
            throws IOException {
        StringReader reader = new StringReader(new String(data, oldCharset));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bos, newCharset);
        IOUtils.copy(reader, writer);
        writer.flush();
        return bos.toByteArray();
    }

    public static String encode(String data, String oldCharset, String newCharset) {
        Charset older = Charset.forName(oldCharset);
        Charset newer = Charset.forName(newCharset);
        return encode(data, older, newer);
    }

    public static String encode(String data, Charset oldCharset, Charset newCharset) {
        return new String(data.getBytes(oldCharset), newCharset);
    }

}
