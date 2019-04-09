package artoria.user;

import artoria.common.AbstractAttributable;

import java.io.Serializable;

/**
 * User information.
 * @author Kahle
 */
public class UserInfo extends AbstractAttributable implements Serializable {
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String ID = "id";

    public String getId() {

        return (String) this.getAttribute(ID);
    }

    public void setId(String id) {

        this.addAttribute(ID, id);
    }

    public String getUsername() {

        return (String) this.getAttribute(USERNAME);
    }

    public void setUsername(String username) {

        this.addAttribute(USERNAME, username);
    }

    public String getPassword() {

        return (String) this.getAttribute(PASSWORD);
    }

    public void setPassword(String password) {

        this.addAttribute(PASSWORD, password);
    }

}
