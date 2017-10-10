package saber.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class JMap extends HashMap<Object, Object> {
    private static final String STRING_CODE = "code";
    private static final String STRING_MSG = "msg";
    private static final String STRING_DATA = "data";

    public static JMap on() {
        return new JMap();
    }

    public static JMap on(Map map) {
        return new JMap().set(map);
    }

    public static JMap on(Object key, Object value) {
        return new JMap().set(key, value);
    }

    private JMap() {}

    public JMap set(Map map) {
        super.putAll(map);
        return this;
    }

    public JMap set(Object key, Object value) {
        super.put(key, value);
        return this;
    }

    public JMap setCode(Object code) {
        super.put(STRING_CODE, code);
        return this;
    }

    public JMap setMsg(Object msg) {
        super.put(STRING_MSG, msg);
        return this;
    }

    public JMap setData(Object data) {
        super.put(STRING_DATA, data);
        return this;
    }

}
