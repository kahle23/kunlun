package saber.selenium;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

public class Downloader {
    private static final Logger log = LoggerFactory.getLogger(Downloader.class);
    private static final int DEFAULT_POOL_SIZE = 1;

    public static Downloader on(DriverType driverType, String driverPath) {
        return on(driverType, driverPath, DEFAULT_POOL_SIZE);
    }

    public static Downloader on(DriverType driverType, String driverPath, int poolSize) {
        if (DriverType.HTMLUNIT.equals(driverType)) {
            throw new RuntimeException("Not finish. ");
        }
        else if (DriverType.PHANTOMJS.equals(driverType)) {
            return phantomjs(driverPath, poolSize);
        }
        else if (DriverType.CHROME.equals(driverType)) {
//            return ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY;
            throw new RuntimeException("Not finish. ");
        }
        else if (DriverType.FIREFOX.equals(driverType)) {
//            return FirefoxDriver.BINARY;
            return null;
        }
        else if (DriverType.EDGE.equals(driverType)) {
//            return EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY;
            throw new RuntimeException("Not finish. ");
        }
        else if (DriverType.IE.equals(driverType)) {
//            return InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY;
            throw new RuntimeException("Not finish. ");
        }
        else if (DriverType.OPERA.equals(driverType)) {
//            return OperaDriverService.OPERA_DRIVER_EXE_PROPERTY;
            throw new RuntimeException("Not finish. ");
        }
        else if (DriverType.SAFARI.equals(driverType)) {
            throw new RuntimeException("Not finish. ");
        }
        else if (DriverType.REMOTE.equals(driverType)) {
            throw new RuntimeException("Not finish. ");
        }
        else {
            throw new RuntimeException("Not finish. ");
        }
    }

    public static Downloader phantomjs(String driverPath, int poolSize) {
        String extension = FilenameUtils.getExtension(driverPath);
        boolean isGhostDriver = "js".equals(extension);
        DesiredCapabilities phantomjs = DesiredCapabilities.phantomjs();
        phantomjs.setJavascriptEnabled(true);
        ArrayList<String> cliArgsCap = new ArrayList<>();
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        phantomjs.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        String[] log = {"--logLevel=INFO"};
        phantomjs.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, log);
        phantomjs.setCapability("takesScreenshot", false);
        String pathProp = PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY;
        pathProp = isGhostDriver ? PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY : pathProp;
        phantomjs.setCapability(pathProp, driverPath);
        WebDriverPool pool = WebDriverPool.on(DriverType.PHANTOMJS, phantomjs, poolSize);
        return new Downloader(pool);
    }

    private final WebDriverPool webDriverPool;
    private int sleepTime = 0;

    private Downloader(WebDriverPool webDriverPool) {
        this.webDriverPool = webDriverPool;
    }

    public WebDriverPool getWebDriverPool() {
        return webDriverPool;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public Downloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public String download(String url) {
        return download(url, null);
    }

    public String download(String url, Map<String, String> cookies) {
        WebDriver webDriver;
        try {
            webDriver = webDriverPool.take();
        }
        catch (InterruptedException e) {
            log.warn("Interrupted ", e);
            return null;
        }
        log.info("Downloading page " + url);
        webDriver.get(url);
        try {
            Thread.sleep(sleepTime);
        }
        catch (InterruptedException e) {
            log.warn("Thread sleep failure. ", e);
        }

        // Cookies
        WebDriver.Options manage = webDriver.manage();
        manage.deleteAllCookies();
        if (MapUtils.isNotEmpty(cookies)) {
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                Cookie cookie = new Cookie(entry.getKey(), entry.getValue());
                manage.addCookie(cookie);
            }
        }

        // Contents
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        webDriverPool.restore(webDriver);
        return content;
    }

    public void close() {
        webDriverPool.destroy();
    }

}
