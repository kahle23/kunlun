/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.event;

import kunlun.action.ActionUtils;
import kunlun.data.mock.MockUtils;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

/**
 * The event collector Test.
 * @author Kahle
 */
public class EventCollectorTest {

    @Test
    public void test1() {
        ActionUtils.execute(Event.of("error:test1")
                .appendMessage("An error has occurred!"));
        ActionUtils.execute(Event.of("info:test1")
                .appendMessage("Info message!"));
    }

    @Test
    public void test2() {
        ActionUtils.execute(Event.of("info:test2")
                .appendMessage("Info message!")
                .putData("user", MockUtils.mock(User.class)));
    }

    @Test
    public void test3() {
        ActionUtils.execute(Event.of("info:test3")
                .setLevel(Event.Level.ERROR)
                .appendMessage("Hello, test3! "));
    }

}
