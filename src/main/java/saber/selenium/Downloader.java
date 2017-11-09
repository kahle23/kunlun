package saber.selenium;

import ch.racic.selenium.drivers.PhantomJSDriverHelper;
import ch.racic.selenium.drivers.exceptions.ExecutableNotFoundException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import saber.util.Reflect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Kahle
 */
public class Downloader {
    private static final String RACIC_PHANTOMJS_DRIVER_HELPER = "ch.racic.selenium.drivers.PhantomJSDriverHelper";
    private static final Logger log = LoggerFactory.getLogger(Downloader.class);
    private static final int DEFAULT_POOL_SIZE = 1;

    public static Downloader on(String driverPath) {
        return on(driverPath, DEFAULT_POOL_SIZE);
    }

    public static Downloader on(String driverPath, int poolSize) {
        DriverType driverType = DriverType.find(driverPath);
        if (driverType == null) {
            throw new RuntimeException("Can not match any driver type. ");
        }
        else {
            log.info("Downloader match \"" + driverType.getDriverClass().getSimpleName() + "\" driver type. ");
        }
        return on(driverType, driverPath, poolSize);
    }

    public static Downloader on(DriverType driverType, String driverPath) {
        return on(driverType, driverPath, DEFAULT_POOL_SIZE);
    }

    public static Downloader on(DriverType driverType, String driverPath, int poolSize) {
        if (DriverType.HTMLUNIT.equals(driverType)) {
            return htmlunit(poolSize);
        }
        else if (DriverType.PHANTOMJS.equals(driverType)) {
            return phantomjs(driverPath, poolSize);
        }
        else if (DriverType.CHROME.equals(driverType)) {
            return chrome(driverPath, poolSize);
        }
        else if (DriverType.FIREFOX.equals(driverType)) {
//            return FirefoxDriver.BINARY;
            throw new RuntimeException("Not finish. ");
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
            throw new RuntimeException("Unexpected enum object. ");
        }
    }

    public static Downloader on(DriverType driverType, DesiredCapabilities dCaps, int poolSize) {
        return new Downloader(WebDriverPool.on(driverType, dCaps, poolSize));
    }

    public static Downloader htmlunit(int poolSize) {
        DesiredCapabilities htmlunit = new DesiredCapabilities();
        htmlunit.setJavascriptEnabled(true);
        htmlunit.setBrowserName("htmlunit");
        WebDriverPool pool = WebDriverPool.on(DriverType.HTMLUNIT, htmlunit, poolSize);
        return new Downloader(pool);
    }

    public static Downloader phantomjs(String driverPath, int poolSize) {
        if (StringUtils.isBlank(driverPath)) {
            if (Reflect.isPresent(RACIC_PHANTOMJS_DRIVER_HELPER, Downloader.class.getClassLoader())) {
                try {
                    driverPath = PhantomJSDriverHelper.executable().toString();
                }
                catch (ExecutableNotFoundException | IOException e) {
                    throw new RuntimeException("Input driver path is blank" +
                            ", and run \"PhantomJSDriverHelper.executable()\" failure. ");
                }
            }
            else {
                throw new RuntimeException("Input driver path is blank. ");
            }
        }
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

    public static Downloader chrome(String driverPath, int poolSize) {
        DesiredCapabilities chrome = DesiredCapabilities.chrome();
        chrome.setJavascriptEnabled(true);
        chrome.setCapability("takesScreenshot", false);
        // The path to the driver executable must be set by the webdriver.chrome.driver system property;
        String pathProp = ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY;
        chrome.setCapability(pathProp, driverPath);
        System.setProperty(pathProp, driverPath);
        WebDriverPool pool = WebDriverPool.on(DriverType.CHROME, chrome, poolSize);
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
