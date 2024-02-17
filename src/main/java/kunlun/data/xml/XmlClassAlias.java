/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.xml;

import java.io.Serializable;

/**
 * The xml class alias.
 * @author Kahle
 */
public class XmlClassAlias implements Serializable {
    private Class<?> type;
    private String alias;

    public XmlClassAlias(String alias, Class<?> type) {
        this.alias = alias;
        this.type = type;
    }

    public XmlClassAlias() {

    }

    public String getAlias() {

        return alias;
    }

    public void setAlias(String alias) {

        this.alias = alias;
    }

    public Class<?> getType() {

        return type;
    }

    public void setType(Class<?> type) {

        this.type = type;
    }

}
