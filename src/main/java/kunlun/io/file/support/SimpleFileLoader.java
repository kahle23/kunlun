/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.file.support;

import kunlun.exception.ExceptionUtils;
import kunlun.io.FileLoader;
import kunlun.util.Assert;
import kunlun.util.ClassLoaderUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The simple file loader (support classpath).
 *
 * @author Kahle
 */
public class SimpleFileLoader implements FileLoader {
    protected static final String CLASSPATH = "classpath:";

    @Override
    public InputStream load(Object param) {
        Assert.notNull(param, "Parameter \"param\" must not null. ");
        String filePath = String.valueOf(param);
        Assert.notNull(filePath, "Variable \"filePath\" must not blank. ");
        try {
            return filePath.startsWith(CLASSPATH) ?
                    ClassLoaderUtils.getResourceAsStream(
                            filePath.substring(CLASSPATH.length()), this.getClass()
                    ) :
                    new FileInputStream(filePath);
        }
        catch (IOException e) { throw ExceptionUtils.wrap(e); }
    }

}
