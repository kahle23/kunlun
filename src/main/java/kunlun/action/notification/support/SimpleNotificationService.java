package kunlun.action.notification.support;

import kunlun.action.notification.AbstractNotificationService;
import kunlun.data.json.JsonUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;

import java.util.Collection;

/**
 * The simple notification service.
 * @author Kahle
 */
public class SimpleNotificationService extends AbstractNotificationService {
    private static final Logger log = LoggerFactory.getLogger(SimpleNotificationService.class);

    @Override
    public <T extends Notification> NotificationRt send(Collection<T> notifications) {
        log.warn("Notification Data: " + JsonUtil.toJsonString(notifications));
        return new NotificationRt();
    }

    /*@Override
    public Map<String, Integer> getUnreadCounts(Object userId, Collection<String> channels) {

        return Collections.emptyMap();
    }

    @Override
    public void markAsRead(Object userId, Collection<String> notificationIds) {

    }

    @Override
    public void markAllAsRead(Object userId, Collection<String> channels) {

    }*/
}
