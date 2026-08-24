/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.model;

import java.io.Serializable;
import java.util.Map;

/**
 * The notification related result object.
 * @author Kahle
 */
public class NotificationRt implements Serializable {
    private Map<String, Object> others;

    public NotificationRt(Map<String, Object> others) {

        this.others = others;
    }

    public NotificationRt() {

    }

    public Map<String, Object> getOthers() {

        return others;
    }

    public void setOthers(Map<String, Object> others) {

        this.others = others;
    }
}
