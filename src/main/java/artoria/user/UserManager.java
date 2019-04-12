package artoria.user;

/**
 * User manager.
 * @author Kahle
 */
public interface UserManager {

    /**
     * User id generation logic.
     * @return User id
     */
    String generateId();

    /**
     * Save user information object.
     * @param userInfo User information object
     */
    void save(UserInfo userInfo);

    /**
     * Remove user information object.
     * @param userId User id
     */
    void remove(String userId);

    /**
     * Refresh user information object.
     * @param userId User id
     */
    void refresh(String userId);

    /**
     * Find user information object.
     * @param userId User id
     * @return User information object
     */
    UserInfo find(String userId);

}
