package artoria.common;

import java.io.Serializable;
import java.util.Date;

/**
 * The data packet.
 * @param <T> The generic for the contents of the packet
 * @author Kahle
 */
public class Packet<T> implements Serializable {
    /**
     * The id of the packet.
     */
    private String id;
    /**
     * Who received the packet.
     * It usually refers to appKey or appId.
     */
    private String user;
    /**
     * The time when the packet was created.
     */
    private Date   time;
    /**
     * The random string.
     */
    private String nonce;
    /**
     * The signature of packet.
     */
    private String sign;
    /**
     * The action of the packet content.
     * Equivalent to "method name" or "data type".
     */
    private String action;
    /**
     * The content carried by the packet.
     */
    private   T    content;

    public Packet() {

    }

    public Packet(T content) {

        this.content = content;
    }

    public Packet(String user, Date time, String action, T content) {
        this.content = content;
        this.action = action;
        this.user = user;
        this.time = time;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {

        this.user = user;
    }

    public Date getTime() {

        return time;
    }

    public void setTime(Date time) {

        this.time = time;
    }

    public String getNonce() {

        return nonce;
    }

    public void setNonce(String nonce) {

        this.nonce = nonce;
    }

    public String getSign() {

        return sign;
    }

    public void setSign(String sign) {

        this.sign = sign;
    }

    public String getAction() {

        return action;
    }

    public void setAction(String action) {

        this.action = action;
    }

    public T getContent() {

        return content;
    }

    public void setContent(T content) {

        this.content = content;
    }

}
