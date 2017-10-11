package sabertest.util;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import saber.util.EncodeUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Arrays;

public class EncodeUtilsTest {

    @Test
    public void test1() throws IOException {
        System.out.println(new String("你好，世界！".getBytes(), "GB2312"));
        StringReader reader = new StringReader("你好，世界！");
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(o, "GB2312");
        IOUtils.copy(reader, writer);
        System.out.println(new String(o.toByteArray(), "GB2312"));
    }

    @Test
    public void test2() throws IOException {
        String data = "你好，世界！";
        System.out.println(new String(data.getBytes(), "GB2312"));
        System.out.println(EncodeUtils.recode(data, "GB2312"));
    }

    @Test
    public void test3() throws IOException {
        byte[] data = "你好，世界！".getBytes();
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(EncodeUtils.recode(data, "utf-8", "GB2312")));
    }

    @Test
    public void test4() throws IOException {
        byte[] data = "你好，世界！".getBytes();
        System.out.println(Arrays.toString(data));
        System.out.println(new String(data));

        byte[] newData = EncodeUtils.recode(data, "utf-8", "ISO-8859-1");
        System.out.println(Arrays.toString(newData));
        System.out.println(new String(newData));

        byte[] oldData = EncodeUtils.recode(newData, "ISO-8859-1", "utf-8");
        System.out.println(Arrays.toString(oldData));
        System.out.println(new String(oldData));
    }

    @Test
    public void test5() throws IOException {
        byte[] data = "你好，世界！".getBytes();
        System.out.println(Arrays.toString(data));
        System.out.println(new String(data));

        byte[] newData = EncodeUtils.recode(data, "utf-8", "gbk");
        System.out.println(Arrays.toString(newData));
        System.out.println(new String(newData));
        System.out.println(new String(newData, "gbk"));

        byte[] oldData = EncodeUtils.recode(newData, "gbk", "utf-8");
        System.out.println(Arrays.toString(oldData));
        System.out.println(new String(oldData));
    }

}
