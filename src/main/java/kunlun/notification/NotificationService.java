package kunlun.notification;

import kunlun.core.Strategy;
import kunlun.notification.model.Notification;
import kunlun.notification.model.NotificationRt;

import java.util.Collection;

/**
 * NotificationService.
 * @author Kahle
 */
public interface NotificationService extends Strategy {

    /**
     * send
     * @param notifications notifications
     * @return result
     */
    <T extends Notification> NotificationRt send(Collection<T> notifications);

}
