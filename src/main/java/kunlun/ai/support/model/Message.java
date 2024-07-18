/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support.model;

import java.io.Serializable;

public class Message implements Serializable {
    public static final String ASSISTANT = "assistant";
    public static final String SYSTEM    = "system";
    public static final String USER      = "user";
    public static final String TOOL      = "tool";
    /**
     * name
     */
    private String name;
    /**
     * system, user, assistant, tool
     */
    private String role;
    /**
     * content
     */
    private Object content;

    public Message(String name, String role, Object content) {
        this.content = content;
        this.role = role;
        this.name = name;
    }

    public Message(String role, Object content) {
        this.content = content;
        this.role = role;
    }

    public Message() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getRole() {

        return role;
    }

    public void setRole(String role) {

        this.role = role;
    }

    public Object getContent() {

        return content;
    }

    public void setContent(Object content) {

        this.content = content;
    }

}
