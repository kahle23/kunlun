package artoria.util;

import artoria.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Arrays;

public class EncoderTest {

    @Test
    public void test1() throws IOException {
        System.out.println(new String("世界，你好！".getBytes(), "GB2312"));
        StringReader reader = new StringReader("世界，你好！");
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(o, "GB2312");
        IOUtils.copyLarge(reader, writer);
        System.out.println(new String(o.toByteArray(), "GB2312"));
    }

    @Test
    public void test2() throws IOException {
        String data = "世界，你好！";
        System.out.println(new String(data.getBytes(), "GB2312"));
        System.out.println(Encoder.recode(data, "GB2312"));
    }

    @Test
    public void test3() throws IOException {
        byte[] data = "世界，你好！".getBytes();
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(Encoder.recode(data, "utf-8", "GB2312")));
    }

    @Test
    public void test4() throws IOException {
        byte[] data = "世界，你好！".getBytes();
        System.out.println(Arrays.toString(data));
        System.out.println(new String(data));

        byte[] newData = Encoder.recode(data, "utf-8", "ISO-8859-1");
        System.out.println(Arrays.toString(newData));
        System.out.println(new String(newData));

        byte[] oldData = Encoder.recode(newData, "ISO-8859-1", "utf-8");
        System.out.println(Arrays.toString(oldData));
        System.out.println(new String(oldData));
    }

    @Test
    public void test5() throws IOException {
        byte[] data = "世界，你好！".getBytes();
        System.out.println(Arrays.toString(data));
        System.out.println(new String(data, "GBK"));
        System.out.println(Arrays.toString(data));

        byte[] newData = Encoder.recode(data, "utf-8", "gbk");
        System.out.println(Arrays.toString(newData));
        System.out.println(new String(newData));
        System.out.println(new String(newData, "gbk"));

        byte[] oldData = Encoder.recode(newData, "gbk", "utf-8");
        System.out.println(Arrays.toString(oldData));
        System.out.println(new String(oldData));
    }

}
