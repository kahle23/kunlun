package sabertest.util;

import org.junit.Test;
import saber.util.FileType;

import java.io.File;

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
        System.out.println(FileType.fileHeader(new File("e:\\1.mp4")).substring(0, 64));
    }

}
