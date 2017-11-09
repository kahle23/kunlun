package saber.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kahle
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class Result extends HashMap<String, Object> {
    private static final String STRING_CODE = "code";
    private static final String STRING_MSG = "msg";
    private static final String STRING_DATA = "data";

    public static Result on() {
        return new Result();
    }

    public static Result on(Integer code) {
        return new Result().setCode(code);
    }

    public static Result on(Map map) {
        return new Result().set(map);
    }

    public static Result on(String key, Object value) {
        return new Result().set(key, value);
    }

    private Result() {}

    public Result set(Map map) {
        super.putAll(map);
        return this;
    }

    public Result set(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Result setCode(Object code) {
        super.put(STRING_CODE, code);
        return this;
    }

    public Result setMsg(Object msg) {
        super.put(STRING_MSG, msg);
        return this;
    }

    public Result setData(Object data) {
        super.put(STRING_DATA, data);
        return this;
    }

}
