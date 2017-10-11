package sabertest.util;

import org.junit.Test;
import saber.util.ThreadUtils;

public class ThreadUtilsTest {

    @Test
    public void test() {
        ThreadUtils.sleepQuietly(10000);
        System.out.println(ThreadUtils.currentLineNumber());
        System.out.println(ThreadUtils.currentMethodName());
        System.out.println(ThreadUtils.currentClassName());
        System.out.println(ThreadUtils.currentThreadName());
        System.out.println(ThreadUtils.currentFileName());
    }

}
