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

public class StorageUtilTest {
    private static Logger log = LoggerFactory.getLogger(StorageUtilTest.class);
    private static final String TEST_KEY = ".\\target\\test\\test_file.txt";

    @Test
    public void test1() {
        StorageUtil.put(DEFAULT, TEST_KEY, "Hello, world! ");
        log.info(JSON.toJSONString(StorageUtil.get(DEFAULT, TEST_KEY, FileEntity.class)));
    }

    @Test
    public void test2() {
        StorageUtil.put(DEFAULT, TEST_KEY, "Hello, world! ");
        log.info("{}", StorageUtil.delete(DEFAULT, TEST_KEY));
    }

    @Test
    public void test3() {
        Collection<FileBase> files = StorageUtil.list(DEFAULT, ".\\target", FileBase.class);
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
        StorageUtil.put("local", testPath, inputFile);
        // Save the file to HuaWei cloud OBS.
        StorageUtil.put("obs",   testPath, inputFile);
        // Save the file to Ali cloud OSS.
        StorageUtil.put("oss",   testPath, inputFile);
        // Save the file to MinIO.
        StorageUtil.put("minio", testPath, inputFile);
    }

}
