package kunlun.notification;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractNotificationService implements NotificationService {

    /**
     * getUnreadCounts
     * @param userId userId
     * @param channels channels
     * @return unreadCounts
     */
    abstract Map<String, Integer> getUnreadCounts(Object userId, Collection<String> channels);

    /**
     * markAsRead
     * @param userId userId
     * @param notificationIds notificationIds
     */
    abstract void markAsRead(Object userId, Collection<String> notificationIds);

    /**
     * markAllAsRead
     * @param userId userId
     * @param channels channels
     */
    abstract void markAllAsRead(Object userId, Collection<String> channels);

}
