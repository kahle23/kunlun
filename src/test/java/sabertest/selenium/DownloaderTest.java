package sabertest.selenium;

import org.junit.Test;
import saber.selenium.Downloader;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.Xsoup;

public class DownloaderTest {

    @Test
    public void test1() {
        // chromedriver.exe
        Downloader phantomjs = Downloader.phantomjs("D:\\Kit\\WebDriver\\phantomjs.exe", 2);
        String download = phantomjs.setSleepTime(9000).download("http://huaban.com/");
        XElements select = Xsoup.select(download, "//*[@id=\"page\"]/div[1]/div[3]/div[2]/div[2]");
        System.out.println(download);
        System.out.println();
        System.out.println();
        System.out.println(select.get());
        phantomjs.close();
    }

}
