/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.storage;

import com.alibaba.fastjson.JSON;
import kunlun.io.FileBase;
import kunlun.io.FileEntity;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

import static kunlun.common.constant.Words.DEFAULT;

public class StorageUtilsTest {
    private static Logger log = LoggerFactory.getLogger(StorageUtilsTest.class);
    private static final String TEST_KEY = ".\\target\\test\\test_file.txt";

    @Test
    public void test1() {
        StorageUtils.put(DEFAULT, TEST_KEY, "Hello, world! ");
        log.info(JSON.toJSONString(StorageUtils.get(DEFAULT, TEST_KEY, FileEntity.class)));
    }

    @Test
    public void test2() {
        StorageUtils.put(DEFAULT, TEST_KEY, "Hello, world! ");
        log.info("{}", StorageUtils.delete(DEFAULT, TEST_KEY));
    }

    @Test
    public void test3() {
        Collection<FileBase> files = StorageUtils.list(DEFAULT, ".\\target", FileBase.class);
        for (FileBase file : files) {
            log.info("{}", JSON.toJSONString(file));
        }
    }

    @Ignore
    @Test
    public void test() {
        // This is just an example, and the configuration part is omitted.
        File inputFile = new File("/test/input_file.txt");
        String testPath = "/test/test_file.txt";
        // Save the file to a local folder.
        StorageUtils.put("local", testPath, inputFile);
        // Save the file to HuaWei cloud OBS.
        StorageUtils.put("obs",   testPath, inputFile);
        // Save the file to Ali cloud OSS.
        StorageUtils.put("oss",   testPath, inputFile);
        // Save the file to MinIO.
        StorageUtils.put("minio", testPath, inputFile);
    }

}
