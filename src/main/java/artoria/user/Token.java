package artoria.user;

import java.io.Serializable;

/**
 * Token object.
 * @author Kahle
 */
public class Token implements Serializable {
    /**
     * Token id.
     */
    private String id;
    /**
     * User id.
     */
    private String userId;
    /**
     * Device information.
     */
    private String device;
    /**
     * Platform information.
     */
    private String platform;
    /**
     * Last accessed time.
     */
    private Long lastAccessedTime;
    /**
     * Last accessed address.
     */
    private String lastAccessedAddress;

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getUserId() {

        return this.userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public String getDevice() {

        return this.device;
    }

    public void setDevice(String device) {

        this.device = device;
    }

    public String getPlatform() {

        return this.platform;
    }

    public void setPlatform(String platform) {

        this.platform = platform;
    }

    public Long getLastAccessedTime() {

        return this.lastAccessedTime;
    }

    public void setLastAccessedTime(Long lastAccessedTime) {

        this.lastAccessedTime = lastAccessedTime;
    }

    public String getLastAccessedAddress() {

        return this.lastAccessedAddress;
    }

    public void setLastAccessedAddress(String lastAccessedAddress) {

        this.lastAccessedAddress = lastAccessedAddress;
    }

}
