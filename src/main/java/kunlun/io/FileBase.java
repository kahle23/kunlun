/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io;

import java.io.Serializable;

/**
 * The base information about the file.
 * @author Kahle
 */
public class FileBase implements Serializable {
    private String name;
    private String addr;

    public FileBase(String name, String addr) {
        this.name = name;
        this.addr = addr;
    }

    public FileBase() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getAddr() {

        return addr;
    }

    public void setAddr(String addr) {

        this.addr = addr;
    }

}
