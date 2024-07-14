/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.util;

import kunlun.common.constant.Symbols;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * The filename tools Test.
 * @author Kahle
 */
public class FilenameUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(FilenameUtilsTest.class);

    @Test
    public void testRootPathAndClasspath() {
        log.info(FilenameUtils.getRootPath());
        log.info(FilenameUtils.getClasspath());
    }

    @Test
    public void testGetExtension() {
        log.info(FilenameUtils.getExtension("C:\\windows\\system\\123.txt"));
        log.info(FilenameUtils.getExtension("C:\\win.dows\\system"));
        log.info(FilenameUtils.getExtension("C:\\windows\\system\\"));
    }

    @Test
    public void testRemoveExtension() {
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\123.txt"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\.anonymous"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\1.anonymous"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\2.anonymous"));
    }

    @Test
    public void testPath() {
        log.info(FilenameUtils.getPackagePath(FilenameUtilsTest.class));
        log.info(FilenameUtils.getClassFilePath(FilenameUtilsTest.class));
    }

    @Test
    public void testNormalize() throws IOException {
        log.info("file separator: {}", Symbols.FILE_SEPARATOR);
        List<String> list = Arrays.asList(
                "C:\\windows/system/123.txt",
                "\\test/windows/system\\123.txt",
                "test\\a/b\\c/system"
        );
        for (String path : list) {
            log.info("Test Path: {}", path);
            log.info("File.getAbsolutePath: {}", new File(path).getAbsolutePath());
            log.info("File.getCanonicalPath: {}", new File(path).getCanonicalPath());
            log.info("FilenameUtils.normalize: {}", FilenameUtils.normalize(path));
        }
    }

}
