package artoria.user;

import artoria.common.AbstractAttributable;

/**
 * User information.
 * @author Kahle
 */
public class UserInfo extends AbstractAttributable {
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String ID = "id";

    public String getId() {

        return (String) this.get(ID);
    }

    public void setId(String id) {

        this.put(ID, id);
    }

    public String getUsername() {

        return (String) this.get(USERNAME);
    }

    public void setUsername(String username) {

        this.put(USERNAME, username);
    }

    public String getPassword() {

        return (String) this.get(PASSWORD);
    }

    public void setPassword(String password) {

        this.put(PASSWORD, password);
    }

}
