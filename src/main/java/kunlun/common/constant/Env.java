/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common.constant;

import kunlun.io.util.FilenameUtils;
import kunlun.net.NetUtils;

/**
 * The common environment constants.
 * @author Kahle
 */
public class Env {

    public static final String COMPUTER_NAME = System.getenv("ComputerName");
    public static final String HOST_NAME = NetUtils.getHostName();
    public static final String ROOT_PATH = FilenameUtils.getRootPath();
    public static final String CLASSPATH = FilenameUtils.getClasspath();

    private Env() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
