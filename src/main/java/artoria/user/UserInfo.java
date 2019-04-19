package artoria.user;

import java.util.HashMap;

/**
 * User information.
 * @author Kahle
 */
public class UserInfo extends HashMap<String, Object> {

    public String getId() {

        return (String) this.get("id");
    }

    public void setId(String id) {

        this.put("id", id);
    }

    public String getUsername() {

        return (String) this.get("username");
    }

    public void setUsername(String username) {

        this.put("username", username);
    }

}
