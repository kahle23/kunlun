/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.file.support;

import kunlun.data.tuple.KeyValue;
import kunlun.data.tuple.Pair;
import kunlun.exception.ExceptionUtil;
import kunlun.io.FileBase;
import kunlun.io.FileObject;
import kunlun.io.storage.AbstractDataStorage;
import kunlun.io.util.FileUtil;
import kunlun.io.util.IoUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.ObjUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static kunlun.common.constant.Charsets.STR_UTF_8;
import static kunlun.common.constant.Symbols.SLASH;

/**
 * The local file storage.
 * @author Kahle
 */
public class LocalFileStorage extends AbstractDataStorage {
    private static final Logger log = LoggerFactory.getLogger(LocalFileStorage.class);
    private final String charset;

    public LocalFileStorage(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
    }

    public LocalFileStorage() {

        this(STR_UTF_8);
    }

    protected File convertToFile(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        if (key instanceof String) {
            Assert.notBlank((String) key, "Parameter \"key\" must not blank. ");
            return new File((String) key);
        }
        else if (key instanceof File) {
            return (File) key;
        }
        else if (key instanceof FileBase) {
            String addr = ((FileBase) key).getAddr();
            Assert.notBlank(addr, "Parameter \"addr\" must not null. ");
            return new File(addr);
        }
        else {
            throw new IllegalArgumentException("Parameter \"key\" is not supported. ");
        }
    }

    @Override
    public boolean exist(Object key) {

        return convertToFile(key).exists();
    }

    @Override
    public FileObject get(Object key) {
        File file = convertToFile(key);
        if (!file.exists()) { return null; }
        Assert.isTrue(file.isFile(), "Parameter \"key\" must correspond to a file. ");
        FileObject fileObject = new FileObject(file.getName(), file.getPath());
        try { fileObject.setContent(new FileInputStream(file)); }
        catch (FileNotFoundException e) {
            throw ExceptionUtil.wrap(e);
        }
        return fileObject;
    }

    @Override
    public Object put(Object data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        InputStream inputStream = null;
        String addr;
        try {
            if (data instanceof FileObject) {
                FileObject fileObject = (FileObject) data;
                inputStream = fileObject.getContent();
                addr = fileObject.getAddr();
            }
            else if (data instanceof KeyValue) {
                @SuppressWarnings("rawtypes")
                KeyValue keyValue = (KeyValue) data;
                inputStream = convertToStream(keyValue.getValue(), charset);
                addr = keyValue.getKey() != null ? String.valueOf(keyValue.getKey()) : null;
            }
            else if (data instanceof Pair) {
                @SuppressWarnings("rawtypes")
                Pair pair = (Pair) data;
                inputStream = convertToStream(pair.getRight(), charset);
                addr = pair.getLeft() != null ? String.valueOf(pair.getLeft()) : null;
            }
            else {
                throw new IllegalArgumentException("Parameter \"data\" is not supported. ");
            }
            Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
            Assert.notNull(addr, "Parameter \"path\" must not null. ");
            return FileUtil.writeFromStream(inputStream, new File(addr));
        }
        catch (Exception e) {
            throw ExceptionUtil.wrap(e);
        }
        finally {
            IoUtil.closeQuietly(inputStream);
        }
    }

    @Override
    public Boolean delete(Object key) {
        File file = convertToFile(key);
        return file.exists() && file.delete();
    }

    @Override
    public Collection<FileBase> list(Object conditions) {
        Assert.notNull(conditions, "Parameter \"conditions\" must not null. ");
        if (ObjUtil.isEmpty(conditions)) { conditions = SLASH; }
        File file = new File(String.valueOf(conditions));
        File[] files = file.listFiles();
        if (files == null) { return null; }
        List<FileBase> list = new ArrayList<FileBase>();
        for (File filePath : files) {
            if (filePath == null) { continue; }
            list.add(new FileBase(filePath.getName(), filePath.getPath()));
        }
        return list;
    }

}
