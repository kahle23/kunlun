package artoria.user;

import artoria.common.AbstractRawData;

import java.io.Serializable;

/**
 * User information.
 * @author Kahle
 */
public class UserInfo extends AbstractRawData implements Serializable {
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

}
