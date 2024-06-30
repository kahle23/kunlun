/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.track;

import kunlun.data.Dict;
import kunlun.data.mock.MockUtils;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

public class TrackUtilsTest {

    @Test
    public void test1() {
        TrackUtils.track("error:test1", "An error has occurred!");
        TrackUtils.track("info:test1", "Info message!");
    }

    @Test
    public void test2() {
        User mock = MockUtils.mock(User.class);
        TrackUtils.track("info:test2", mock);
    }

    @Test
    public void test3() {
        TrackUtils.track(Dict.of("code", "info:test3")
                .set("summary", "Hello, test3! "));
    }

}
