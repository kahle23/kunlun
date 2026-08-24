package kunlun.action.notification;

import kunlun.core.Action;
import kunlun.notification.NotificationService;
import kunlun.notification.model.Notification;
import kunlun.util.IterUtil;

import java.util.Collection;

import static java.util.Collections.singletonList;

/**
 * The abstract notification service.
 * @author Kahle
 */
public abstract class AbstractNotificationService implements NotificationService, Action {
    /**
     * The standard operation name: send.
     */
    protected static final String SEND = "send";

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        if (SEND.equals(strategy)) {
            if (input instanceof Collection) {
                //noinspection unchecked
                return send((Collection<Notification>) input);
            } else {
                return send(singletonList((Notification) input));
            }
//        } else if () {
        } else {
            if (input instanceof Collection &&
                    (IterUtil.getFirst((Collection<?>) input) instanceof Notification)) {
                //noinspection unchecked
                return send((Collection<Notification>) input);
            } else if (input instanceof Notification) {
                return send(singletonList((Notification) input));
            } else {
                throw new IllegalArgumentException("Unsupported input parameters type. ");
            }
        }
    }

}
