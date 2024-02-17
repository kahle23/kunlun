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
public interface FileEntity extends FileBase {

    /**
     * Get the input stream of the file.
     * @return The input stream of the file
     */
    InputStream getInputStream();

}
