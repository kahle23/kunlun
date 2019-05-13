package artoria.user;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User information.
 * @author Kahle
 */
public class UserInfo implements Serializable {
    private Map<String, Object> rawData = new HashMap<String, Object>();
    private String username;
    private String id;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public Object get(String key) {

        return rawData.get(key);
    }

    public Object put(String key, Object value) {

        return rawData.put(key, value);
    }

    public Object remove(String key) {

        return rawData.remove(key);
    }

    public Map<String, Object> rawData() {

        return Collections.unmodifiableMap(rawData);
    }

    public void putAll(Map<String, Object> data) {

        rawData.putAll(data);
    }

    public void clear() {

        rawData.clear();
    }

}
