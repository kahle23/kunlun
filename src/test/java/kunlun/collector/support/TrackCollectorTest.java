/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.collector.support;

import kunlun.collector.CollectorUtils;
import kunlun.collector.support.model.TrackRecord;
import kunlun.data.mock.MockUtils;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

public class TrackCollectorTest {

    @Test
    public void test1() {
        CollectorUtils.collect(TrackRecord.of("error:test1")
                .appendMessage("An error has occurred!"));
        CollectorUtils.collect(TrackRecord.of("info:test1")
                .appendMessage("Info message!"));
    }

    @Test
    public void test2() {
        CollectorUtils.collect(TrackRecord.of("info:test2")
                .appendMessage("Info message!")
                .putData("user", MockUtils.mock(User.class)));
    }

    @Test
    public void test3() {
        CollectorUtils.collect(TrackRecord.of("info:test3")
                .appendMessage("Hello, test3! "));
    }

}
