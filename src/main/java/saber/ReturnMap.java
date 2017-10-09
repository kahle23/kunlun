package saber;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class ReturnMap extends HashMap<Object, Object> {
    private static final String STRING_CODE = "code";
    private static final String STRING_MSG = "msg";
    private static final String STRING_DATA = "data";

    public static ReturnMap kv() {
        return new ReturnMap();
    }

    public static ReturnMap kv(Map map) {
        return new ReturnMap().set(map);
    }

    public static ReturnMap kv(Object key, Object value) {
        return new ReturnMap().set(key, value);
    }

    private ReturnMap() {}

    public ReturnMap set(Map map) {
        super.putAll(map);
        return this;
    }

    public ReturnMap set(Object key, Object value) {
        super.put(key, value);
        return this;
    }

    public ReturnMap setCode(Object code) {
        super.put(STRING_CODE, code);
        return this;
    }

    public ReturnMap setMsg(Object msg) {
        super.put(STRING_MSG, msg);
        return this;
    }

    public ReturnMap setData(Object data) {
        super.put(STRING_DATA, data);
        return this;
    }

}
