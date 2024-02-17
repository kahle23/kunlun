/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.file.support;

import kunlun.io.FileBase;
import kunlun.util.Assert;

/**
 * The file basic information implementation class.
 * @author Kahle
 */
public class FileBaseImpl implements FileBase {
    private String charset;
    private String path;
    private String name;

    public FileBaseImpl(String name, String path) {
        Assert.notNull(path, "Parameter \"path\" must not null. ");
        this.path = path;
        this.name = name;
    }

    public FileBaseImpl(String path) {

        this(null, path);
    }

    public FileBaseImpl() {

    }

    @Override
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    @Override
    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;
    }

    @Override
    public String getCharset() {

        return charset;
    }

    public void setCharset(String charset) {

        this.charset = charset;
    }

}
