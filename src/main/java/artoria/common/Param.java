package artoria.common;

import java.io.Serializable;

/**
 * Uniform parameter input object.
 * @param <T> Data type
 * @author Kahle
 */
public class Param<T> implements Serializable {
    private Object currentUser;
    private Object targetUser;
    private Paging paging;
    private T data;

    public Param() {
    }

    public Param(T data) {

        this.data = data;
    }

    public Param(Paging paging, T data) {
        this.paging = paging;
        this.data = data;
    }

    public Object getCurrentUser() {

        return this.currentUser;
    }

    public void setCurrentUser(Object currentUser) {

        this.currentUser = currentUser;
    }

    public Object getTargetUser() {

        return this.targetUser;
    }

    public void setTargetUser(Object targetUser) {

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
