/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.file.support;

import com.alibaba.fastjson.JSON;
import kunlun.io.FileBase;
import kunlun.io.FileEntity;
import kunlun.io.util.IOUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.CloseUtils;
import kunlun.util.CollectionUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class LocalFileStorageTest {
    private static Logger log = LoggerFactory.getLogger(LocalFileStorageTest.class);
    private static LocalFileStorage localFileStorage = new LocalFileStorage();
    private static String testPath = ".\\target\\test\\test_file.txt";

    @Test
    public void test1() {
        FileEntityImpl fileEntity = new FileEntityImpl(testPath);
        fileEntity.setInputStream(new ByteArrayInputStream("Hello, world! ".getBytes()));
        Object put = localFileStorage.put(fileEntity);
        log.info("put: {}", put);
        put = localFileStorage.put(testPath, "Hello, Hello, world! ");
        log.info("put: {}", put);
    }

    @Test
    public void test2() throws IOException {
        // Put file.
        FileEntityImpl filePut = new FileEntityImpl(testPath);
        filePut.setInputStream(new ByteArrayInputStream("Hello, world! ".getBytes()));
        Object put = localFileStorage.put(filePut);
        log.info("put: {}", put);
        // Get file and exist file.
        boolean exist = localFileStorage.exist(testPath);
        FileEntity fileGet = localFileStorage.get(testPath);
        InputStream inputStream = fileGet.getInputStream();
        String content = IOUtils.toString(inputStream);
        CloseUtils.closeQuietly(inputStream);
        String name = fileGet.getName();
        String path = fileGet.getPath();
        log.info("exist: {}, name: {}, path: {}, content: {}", exist, name, path, content);
        // Delete file and exist file.
        Boolean delete = localFileStorage.delete(testPath);
        exist = localFileStorage.exist(testPath);
        log.info("exist: {}, delete: {}", exist, delete);
    }

    @Test
    public void test3() {
        Collection<FileBase> list = localFileStorage.list(".\\target");
        if (CollectionUtils.isEmpty(list)) { return; }
        for (FileBase fileBase : list) {
            log.info("file: {}", JSON.toJSONString(fileBase));
        }
    }

}
