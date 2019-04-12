package artoria.user;

import java.util.List;

/**
 * Token manager.
 * @author Kahle
 */
public interface TokenManager {

    /**
     * Token id generation logic.
     * @return Token id
     */
    String generateId();

    /**
     * Save token object.
     * @param token Token object
     */
    void save(Token token);

    /**
     * Remove token object.
     * @param tokenId Token id
     */
    void remove(String tokenId);

    /**
     * Refresh token object.
     * @param tokenId Token id
     */
    void refresh(String tokenId);

    /**
     * Find token object.
     * @param tokenId Token id
     * @return Token object
     */
    Token find(String tokenId);

    /**
     * Query token object list.
     * @param userId User id
     * @return Token object list
     */
    List<Token> query(String userId);

}
