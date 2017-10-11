package sabertest.crypto;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import saber.crypto.HashUtils;

import java.io.File;

public class HashUtilsTest {

    @Test
    public void test1() throws Exception {
        System.out.println(HashUtils.md5("123456"));
        System.out.println(HashUtils.sha1("123456"));
        System.out.println(HashUtils.sha256("123456"));
        System.out.println(HashUtils.sha384("123456"));
        System.out.println(HashUtils.sha512("123456"));
    }

    @Test
    public void test2() throws Exception {
        File file = new File("D:\\");
        File[] files = file.listFiles();
        if (ArrayUtils.isEmpty(files)) {
            System.out.println(file + "下没有文件！");
            return;
        }
        for (File file1 : files) {
            if (file1.isFile()) {
                System.out.println(HashUtils.md5f(file1));
                System.out.println(HashUtils.sha1f(file1));
                System.out.println(HashUtils.sha256f(file1));
                System.out.println(HashUtils.sha384f(file1));
                System.out.println(HashUtils.sha512f(file1));
            }
        }

    }

}
