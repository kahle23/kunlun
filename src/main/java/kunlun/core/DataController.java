package kunlun.core;

public interface DataController extends Strategy {

    /**
     * Get rule.
     * @param permission permission
     * @param userId The unique identification of the user
     * @param userType The user type (can null) (for example, toC user, toB user, or admin user)
     * @param <T> Rule
     * @return Rule
     */
    <T extends Rule> T getRule(String permission, Object userId, Object userType);

    interface Rule {

    }

}
