package com.github.kahlkn.artoria.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.lang.management.ThreadInfo;

public class ThreadUtilsTest {

    @Test
    public void test() {
        // ThreadUtils.sleepQuietly(1000);
        System.out.println(ThreadUtils.getThreadName());
        ThreadInfo threadInfo = ThreadUtils.getThreadInfo(1);
        System.out.println(JSON.toJSONString(threadInfo, true));
    }

}
