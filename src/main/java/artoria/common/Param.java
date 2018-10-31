package artoria.common;

import java.io.Serializable;

/**
 * Uniform parameter input object.
 * @param <T> Data type
 * @author Kahle
 */
public class Param<T extends Serializable> implements Serializable {
    private Object currentUser;
    private Object targetUser;
    private Paging paging;
    private T data;

    public Param() {
    }

    public Param(T data) {

        this.data = data;
    }

    public Param(T data, Paging paging) {
        this.paging = paging;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public <R extends Serializable> R getCurrentUser() {

        return (R) currentUser;
    }

    public <P extends Serializable> void setCurrentUser(P currentUser) {

        this.currentUser = currentUser;
    }

    @SuppressWarnings("unchecked")
    public <R extends Serializable> R getTargetUser() {

        return (R) targetUser;
    }

    public <P extends Serializable> void setTargetUser(P targetUser) {

        this.targetUser = targetUser;
    }

    public Paging getPaging() {

        return this.paging;
    }

    public void setPaging(Paging paging) {

        this.paging = paging;
    }

    public T getData() {

        return this.data;
    }

    public void setData(T data) {

        this.data = data;
    }

}
