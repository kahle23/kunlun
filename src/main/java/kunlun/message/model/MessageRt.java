/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.model;

import java.io.Serializable;
import java.util.Map;

/**
 * The message related result object.
 * @author Kahle
 */
public class MessageRt implements Serializable {
    private Map<String, Object> others;

    public MessageRt(Map<String, Object> others) {

        this.others = others;
    }

    public MessageRt() {

    }

    public Map<String, Object> getOthers() {

        return others;
    }

    public void setOthers(Map<String, Object> others) {

        this.others = others;
    }
}
