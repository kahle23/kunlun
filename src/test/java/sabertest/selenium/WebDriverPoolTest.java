package sabertest.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import saber.selenium.DriverType;
import saber.selenium.WebDriverPool;

import java.util.ArrayList;

public class WebDriverPoolTest {

    @Test
    public void test1() throws Exception {
        // chromedriver.exe
        DesiredCapabilities phantomjs = new DesiredCapabilities();
        phantomjs.setJavascriptEnabled(true);
        ArrayList<String> cliArgsCap = new ArrayList<>();
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        phantomjs.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        phantomjs.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[] { "--logLevel=INFO" });
        phantomjs.setCapability("takesScreenshot", false);
        phantomjs.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "D:\\Kit\\WebDriver\\phantomjs.exe");

        WebDriverPool pool = WebDriverPool.on(DriverType.PHANTOMJS, phantomjs);
        WebDriver driver = pool.take();
        driver.get("http://huaban.com/");
        WebElement webElement = driver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        WebElement element = webElement.findElement(By.xpath("//*[@id=\"page\"]/div[1]/div[3]/div[2]/div[2]"));
        String outerHTML = element.getAttribute("outerHTML");
        System.out.println(content);
        System.out.println();
        System.out.println(outerHTML);

        pool.restore(driver);
        Thread.sleep(9000);
        pool.destroy();
    }

}
