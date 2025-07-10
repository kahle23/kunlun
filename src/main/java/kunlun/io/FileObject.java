/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io;

import java.io.InputStream;

/**
 * The entity object of the file.
 * @author Kahle
 */
public class FileObject extends FileBase {
    private String charset;
    private String mediaType;
    private Long   size;
    private InputStream content;

    public FileObject(String name, InputStream content) {
        super(name, null);
        this.content = content;
    }

    public FileObject(String name, String addr) {

        super(name, addr);
    }

    public FileObject() {

    }

    public String getCharset() {

        return charset;
    }

    public void setCharset(String charset) {

        this.charset = charset;
    }

    public String getMediaType() {

        return mediaType;
    }

    public void setMediaType(String mediaType) {

        this.mediaType = mediaType;
    }

    public Long getSize() {

        return size;
    }

    public void setSize(Long size) {

        this.size = size;
    }

    public InputStream getContent() {

        return content;
    }

    public void setContent(InputStream content) {

        this.content = content;
    }
}
