package artoria.track;

import artoria.data.Dict;
import artoria.mock.MockUtils;
import artoria.test.pojo.entity.system.User;
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
