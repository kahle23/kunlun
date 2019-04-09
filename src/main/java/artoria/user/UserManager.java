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
     * Kick out user information object.
     * @param userId User id
     */
    void kickout(String userId);

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

    /**
     * Save user information object.
     * @param userInfo User information object
     * @return User id
     */
    String save(UserInfo userInfo);

}
