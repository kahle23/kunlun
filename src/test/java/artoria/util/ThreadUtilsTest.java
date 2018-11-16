package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.lang.management.ThreadInfo;

public class ThreadUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ThreadUtilsTest.class);

    @Test
    public void test() {
        // ThreadUtils.sleepQuietly(1000);
        log.info(ThreadUtils.getThreadName());
        ThreadInfo threadInfo = ThreadUtils.getThreadInfo(1);
        log.info(JSON.toJSONString(threadInfo, true));
    }

}
