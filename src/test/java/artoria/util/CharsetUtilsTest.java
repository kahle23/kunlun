package artoria.util;

import artoria.io.util.IOUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Arrays;

public class CharsetUtilsTest {
    private static Logger log = LoggerFactory.getLogger(CharsetUtilsTest.class);

    @Test
    public void test1() throws IOException {
        log.info(new String("世界，你好！".getBytes(), "GB2312"));
        StringReader reader = new StringReader("世界，你好！");
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(o, "GB2312");
        IOUtils.copyLarge(reader, writer);
        log.info(new String(o.toByteArray(), "GB2312"));
    }

    @Test
    public void test2() throws IOException {
        String data = "世界，你好！";
        log.info(new String(data.getBytes(), "GB2312"));
        log.info(CharsetUtils.recode(data, "GB2312"));
    }

    @Test
    public void test3() throws IOException {
        byte[] data = "世界，你好！".getBytes();
        log.info(Arrays.toString(data));
        log.info(Arrays.toString(CharsetUtils.recode(data, "utf-8", "GB2312")));
    }

    @Test
    public void test4() throws IOException {
        byte[] data = "世界，你好！".getBytes();
        log.info(Arrays.toString(data));
        log.info(new String(data));

        byte[] newData = CharsetUtils.recode(data, "utf-8", "ISO-8859-1");
        log.info(Arrays.toString(newData));
        log.info(new String(newData));

        byte[] oldData = CharsetUtils.recode(newData, "ISO-8859-1", "utf-8");
        log.info(Arrays.toString(oldData));
        log.info(new String(oldData));
    }

    @Test
    public void test5() throws IOException {
        byte[] data = "世界，你好！".getBytes();
        log.info(Arrays.toString(data));
        log.info(new String(data, "GBK"));
        log.info(Arrays.toString(data));

        byte[] newData = CharsetUtils.recode(data, "utf-8", "gbk");
        log.info(Arrays.toString(newData));
        log.info(new String(newData));
        log.info(new String(newData, "gbk"));

        byte[] oldData = CharsetUtils.recode(newData, "gbk", "utf-8");
        log.info(Arrays.toString(oldData));
        log.info(new String(oldData));
    }

}
