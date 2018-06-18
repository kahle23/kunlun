package artoria.io;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

@Ignore
public class FileUtilsTest {

    @Test
    public void testRenameTo() {
        File file = new File("E:\\hello.txt");
        boolean rnSc = file.renameTo(new File("D:\\123.txt"));
        System.out.println(rnSc);

        file = new File("D:\\123.txt");
        rnSc = file.renameTo(new File("E:\\hello.txt"));
        System.out.println(rnSc);
    }

    @Test
    public void testRename() {
        File file = new File("E:\\hello.txt");
        System.out.println(FileUtils.rename(file, "123.txt"));
        file = new File("E:\\123.txt");
        System.out.println(FileUtils.rename(file, "hello.txt"));
    }

    @Test
    public void testDeleteDirectory() throws IOException {
        File dest = new File("E:\\test");
        FileUtils.deleteDirectory(dest);
    }

    @Test
    public void testCopyFileToFile() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest\\11.txt");
        FileUtils.copyFileToFile(src, dest, false);
    }

    @Test
    public void testCopyFileToDirectory() throws IOException {
        File src = new File("E:\\src\\1.txt");
        File dest = new File("E:\\dest");
        FileUtils.copyFileToDirectory(src, dest, false);
    }

    @Test
    public void testCopyDirectoryToDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtils.copyDirectoryToDirectory(src, dest);
    }

    @Test
    public void testMoveDirectory() throws IOException {
        File src = new File("E:\\src");
        File dest = new File("E:\\dest");
        FileUtils.moveDirectory(src, dest);
    }

}
