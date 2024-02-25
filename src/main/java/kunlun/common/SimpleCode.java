/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import kunlun.data.CodeDefinition;

public class SimpleCode implements CodeDefinition {
    private String description;
    private Object code;

    public SimpleCode(Object code, String description) {
        this.code = code;
        this.description = description;
    }

    public SimpleCode() {

    }

    @Override
    public Object getCode() {

        return code;
    }

    public void setCode(Object code) {

        this.code = code;
    }

    @Override
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public String toString() {
        return "SimpleCode{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
