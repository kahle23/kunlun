/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import com.alibaba.fastjson.JSON;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.lang.management.ThreadInfo;

import static kunlun.common.constant.Numbers.ONE;

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
