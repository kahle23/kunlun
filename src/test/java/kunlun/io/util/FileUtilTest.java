/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

@Ignore
public class FileUtilTest {
    private static Logger log = LoggerFactory.getLogger(FileUtilTest.class);

    @Test
    public void testRenameTo() {
        File file = new File("E:\\hello.txt");
        boolean rnSc = file.renameTo(new File("D:\\123.txt"));
        log.info("{}", rnSc);

        file = new File("D:\\123.txt");
        rnSc = file.renameTo(new File("E:\\hello.txt"));
        log.info("{}", rnSc);
    }

    @Test
    public void testRename() {
        File file = new File("E:\\hello.txt");
        log.info("{}", FileUtil.rename(file, "123.txt"));
        file = new File("E:\\123.txt");
        log.info("{}", FileUtil.rename(file, "hello.txt"));
    }

    @Test
    public void testWrite() {
        File destination = new File("e:\\test.txt");
        FileUtil.writeUtf8String("Hello, World! ", destination);
    }

    @Test
    public void testDeleteDirectory() throws IOException {
        File dest = new File("E:\\test");
        FileUtil.deleteDirectory(dest);
    }

    @Test
    public void testCopyFileToFile() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest\\11.txt");
        FileUtil.copyFileToFile(src, dest, false);
    }

    @Test
    public void testCopyFileToDirectory() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest");
        FileUtil.copyFileToDirectory(src, dest, false);
    }

    @Test
    public void testCopyDirectoryToDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtil.copyDirectoryToDirectory(src, dest);
    }

    @Test
    public void testMoveDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtil.moveDirectory(src, dest);
    }

}
