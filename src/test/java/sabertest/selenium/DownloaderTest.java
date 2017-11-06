package sabertest.selenium;

import org.junit.Test;
import saber.selenium.Downloader;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.Xsoup;

public class DownloaderTest {

    @Test
    public void phantomjsTest() {
        // Downloader phantomjs = Downloader.phantomjs("D:\\Kit\\WebDriver\\phantomjs.exe", 2);
        Downloader phantomjs = Downloader.on("D:\\Kit\\WebDriver\\phantomjs.exe");
        String download = phantomjs.setSleepTime(9000).download("http://huaban.com/");
        XElements select = Xsoup.select(download, "//*[@id=\"page\"]/div[1]/div[3]/div[2]/div[2]");
        System.out.println(download);
        System.out.println();
        System.out.println();
        System.out.println(select.get());
        phantomjs.close();
    }

    @Test
    public void chromeTest() {
        // Downloader phantomjs = Downloader.phantomjs("D:\\Kit\\WebDriver\\chromedriver.exe", 2);
        Downloader phantomjs = Downloader.on("D:\\Kit\\WebDriver\\chromedriver.exe");
        String download = phantomjs.setSleepTime(9000).download("http://huaban.com/");
        XElements select = Xsoup.select(download, "//*[@id=\"page\"]/div[1]/div[3]/div[2]/div[2]");
        System.out.println(select.get());
        phantomjs.close();
    }

}
