package artoriatest.util;

import artoria.util.FileUtils;
import org.junit.Test;

import java.io.File;

public class FileUtilsTest {

    @Test
    public void test_renameTo() {
        File file = new File("E:\\hello.txt");
        boolean rnSc = file.renameTo(new File("D:\\123.txt"));
        System.out.println(rnSc);

        file = new File("D:\\123.txt");
        rnSc = file.renameTo(new File("E:\\hello.txt"));
        System.out.println(rnSc);
    }

    @Test
    public void test_rename() {
        File file = new File("E:\\hello.txt");
        System.out.println(FileUtils.rename(file, "123.txt"));
        file = new File("E:\\123.txt");
        System.out.println(FileUtils.rename(file, "hello.txt"));
    }


}
