/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.model;

import java.io.Serializable;
import java.util.Map;

/**
 * The subscribe related result object.
 * @author Kahle
 */
public class SubscribeRt implements Serializable {
    private Map<String, Object> others;

    public SubscribeRt(Map<String, Object> others) {

        this.others = others;
    }

    public SubscribeRt() {

    }

    public Map<String, Object> getOthers() {

        return others;
    }

    public void setOthers(Map<String, Object> others) {

        this.others = others;
    }
}
