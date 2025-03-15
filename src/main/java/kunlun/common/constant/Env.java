/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common.constant;

import kunlun.io.util.FilenameUtil;
import kunlun.net.NetUtil;

/**
 * The common environment constants.
 * @author Kahle
 */
public class Env {

    public static final String COMPUTER_NAME = System.getenv("ComputerName");
    public static final String HOST_NAME = NetUtil.getHostName();
    public static final String ROOT_PATH = FilenameUtil.getRootPath();
    public static final String CLASSPATH = FilenameUtil.getClasspath();
    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    private Env() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
