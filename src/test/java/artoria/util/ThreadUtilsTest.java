package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.lang.management.ThreadInfo;

import static artoria.common.constant.Numbers.ONE;

public class ThreadUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ThreadUtilsTest.class);

    @Test
    public void test() {
        // ThreadUtils.sleepQuietly(ONE_THOUSAND);
        log.info(ThreadUtils.getThreadName());
        ThreadInfo threadInfo = ThreadUtils.getThreadInfo(ONE);
        log.info(JSON.toJSONString(threadInfo, true));
    }

}
