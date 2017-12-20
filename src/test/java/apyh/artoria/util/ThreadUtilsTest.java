package apyh.artoria.util;

import org.junit.Test;

public class ThreadUtilsTest {

    @Test
    public void test() {
        ThreadUtils.sleepQuietly(1000);
        System.out.println(ThreadUtils.getLineNumber());
        System.out.println(ThreadUtils.getMethodName());
        System.out.println(ThreadUtils.getClassName());
        System.out.println(ThreadUtils.getThreadName());
        System.out.println(ThreadUtils.getFileName());
    }

}
