package sabertest.util;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import saber.util.RuntimeUtils;

import java.io.IOException;
import java.io.InputStream;

public class RuntimeUtilsTest {

    @Test
    public void test1() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process exec = runtime.exec("netstat -ano");
        InputStream in = exec.getInputStream();
        System.out.println(IOUtils.toString(in, "GB2312"));
    }

    @Test
    public void test2() throws IOException {
//        String cmd = "ping www.baidu.com";
        String cmd = "netstat -ano";
        String charset = "GB2312";
        Process process = RuntimeUtils.doExec(cmd);
        System.out.println(RuntimeUtils.exec(process, charset));
    }

    @Test
    public void test2_1() throws IOException {
        Process process = RuntimeUtils.doExec(new String[]{"ping","www.baidu.com"});
        System.out.println(RuntimeUtils.exec(process, "GB2312"));
    }

    @Test
    public void test3() throws IOException {
        String cmd = "ping -t www.baidu.com";
        String charset = "GB2312";
        Process process = RuntimeUtils.doExec(cmd);
        System.out.println(RuntimeUtils.exec(process, 1000L, charset));
    }

    @Test
    public void test4() throws IOException {
        String cmd = "netstat -ano";
        String charset = "GB2312";
        System.out.println(RuntimeUtils.exec(cmd, charset));
    }

    @Test
    public void test5() throws IOException {
        String cmd = "ping -t www.baidu.com";
        String charset = "GB2312";
        System.out.println(RuntimeUtils.exec(cmd, 1000L, charset));
    }

}
