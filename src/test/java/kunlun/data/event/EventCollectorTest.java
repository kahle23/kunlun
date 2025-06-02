/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.event;

import kunlun.action.ActionUtil;
import kunlun.data.Event;
import kunlun.data.mock.MockUtil;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

/**
 * The event collector Test.
 * @author Kahle
 */
public class EventCollectorTest {

    @Test
    public void test1() {
        ActionUtil.execute(Event.of("error:test1")
                .appendMessage("An error has occurred!"));
        ActionUtil.execute(Event.of("info:test1")
                .appendMessage("Info message!"));
    }

    @Test
    public void test2() {
        ActionUtil.execute(Event.of("info:test2")
                .appendMessage("Info message!")
                .putData("user", MockUtil.mock(User.class)));
    }

    @Test
    public void test3() {
        ActionUtil.execute(Event.of("info:test3")
                .setLevel(Event.Level.ERROR)
                .appendMessage("Hello, test3! "));
    }

}
