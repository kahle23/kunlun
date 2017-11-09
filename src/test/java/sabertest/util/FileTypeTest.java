package sabertest.util;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import saber.codec.Hex;
import saber.util.FileType;

import java.io.File;
import java.util.Arrays;

public class FileTypeTest {

    @Test
    public void test1() throws Exception {
        System.out.println(FileType.check(new File("e:\\1.doc")));
        System.out.println(FileType.check(new File("e:\\1.xls")));
        System.out.println(FileType.check(new File("e:\\1.ppt")));
        System.out.println(FileType.check(new File("e:\\1.docx")));
        System.out.println(FileType.check(new File("e:\\1.xlsx")));
        System.out.println(FileType.check(new File("e:\\1.pptx")));
        System.out.println(FileType.check(new File("e:\\1.jar")));
    }

    @Test
    public void test2() throws Exception {
//        System.out.println(FileType.check(new File("e:\\1.exe")));
//        System.out.println(FileType.check(new File("e:\\1.rar")));
//        System.out.println(FileType.check(new File("e:\\1.zip")));
//        System.out.println(FileType.check(new File("e:\\1.rtf")));
//        System.out.println(FileType.check(new File("e:\\1.mp4")));
//        System.out.println(FileType.check(new File("e:\\1.class")));
        System.out.println(FileType.fileHeader(new File("e:\\1.class")).substring(0, 64));
    }

    @Test
    public void testClass() throws Exception {
        byte[] bytes = FileUtils.readFileToByteArray(new File("e:\\1.class"));
        // class文件前四位为魔数位，魔数位的值一般固定为 CAFEBABE
        String magic = Hex.ME.encodeToString(Arrays.copyOfRange(bytes, 0, 4));
        System.out.println("魔数为：" + magic.toUpperCase());

        // 次版本号
        int minorVersion = (((int)bytes[4]) << 8) + bytes[5];
        // 主版本号
        int majorVersion = (((int)bytes[6]) << 8) + bytes[7];
        System.out.println(bytes[4] + " | " + bytes[5] + " | " + bytes[6] + " | " + bytes[7]);
        System.out.println("主版本号为：" + majorVersion + "，次版本号为：" + minorVersion);
    }

}
