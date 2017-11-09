package sabertest.util;

import org.junit.Test;
import saber.util.Result;

import java.util.HashMap;
import java.util.Map;

public class ResultTest {

    @Test
    public void test1() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "bbb");
        System.out.println(Result.on().setCode(0).setData(map));
        System.out.println(Result.on().setCode(2010).setData("hello!"));
    }

}
