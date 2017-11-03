package saber.selenium;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class WebDriverPool {
    private final static Logger log = LoggerFactory.getLogger(WebDriverPool.class);
    private final static String REMOTE_WEB_URL_PROPERTY = "remote.web.url";
    private final static int DEFAULT_CAPACITY = 5;
    private final static int STAT_DESTROYED = 2;
    private final static int STAT_RUNNING = 1;

    public static WebDriverPool on(DriverType driverType, DesiredCapabilities dCaps) {
        return new WebDriverPool(driverType, dCaps, DEFAULT_CAPACITY);
    }

    public static WebDriverPool on(DriverType driverType, DesiredCapabilities dCaps, int capacity) {
        return new WebDriverPool(driverType, dCaps, capacity);
    }

    // store webDrivers created
    private List<WebDriver> webDriverList = Collections.synchronizedList(new ArrayList<WebDriver>());
    // store webDrivers available
    private BlockingDeque<WebDriver> innerQueue = new LinkedBlockingDeque<>();
    private AtomicInteger stat = new AtomicInteger(STAT_RUNNING);
    private final DesiredCapabilities dCaps;
    private final DriverType driverType;
    private final int capacity;

    private WebDriverPool(DriverType driverType, DesiredCapabilities dCaps, int capacity) {
        this.driverType = driverType;
        this.dCaps = dCaps;
        this.capacity = capacity;
    }

    private void checkRunning() {
        if (!stat.compareAndSet(STAT_RUNNING, STAT_RUNNING)) {
            throw new IllegalStateException("Already destroyed!");
        }
    }

    private WebDriver createDriver() throws IOException {
        if (DriverType.REMOTE.equals(driverType)) {
            String urlString = (String) dCaps.getCapability(REMOTE_WEB_URL_PROPERTY);
            URL url;
            try {
                url = new URL(urlString);
            }
            catch (MalformedURLException mue) {
                throw new IllegalArgumentException("DesiredCapabilities object must have \""
                        + REMOTE_WEB_URL_PROPERTY + "\" property, if driver type is \"REMOTE\". ");
            }
            if (StringUtils.isBlank(dCaps.getBrowserName())) {
                dCaps.setBrowserName("phantomjs");
            }
            return new RemoteWebDriver(url, dCaps);
        }
        else {
            try {
                Class<? extends WebDriver> clz = driverType.getDriverClass();
                Constructor<? extends WebDriver> cst = clz.getConstructor(Capabilities.class);
                return cst.newInstance(dCaps);
            }
            catch (ReflectiveOperationException e) {
                throw new RuntimeException("Class \"" + driverType.getDriverClass()
                        + "\" and parameter type \"" + Capabilities.class + "\" create instance failure. ", e);
            }
        }
    }

    public WebDriver take() throws InterruptedException {
        checkRunning();
        WebDriver poll = innerQueue.poll();
        if (poll != null) {
            return poll;
        }
        if (webDriverList.size() < capacity) {
            synchronized (webDriverList) {
                if (webDriverList.size() < capacity) {
                    // add new WebDriver instance into pool
                    try {
                        WebDriver driver = createDriver();
                        innerQueue.add(driver);
                        webDriverList.add(driver);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //
                }
            }
        }
        return innerQueue.take();
    }

    public void restore(WebDriver webDriver) {
        checkRunning();
        innerQueue.add(webDriver);
    }

    public void destroy() {
        boolean b = stat.compareAndSet(STAT_RUNNING, STAT_DESTROYED);
        if (!b) {
            throw new IllegalStateException("Already destroyed!");
        }
        for (WebDriver webDriver : webDriverList) {
            log.info("Quit webDriver" + webDriver);
            webDriver.quit();
            webDriver = null;
        }
    }

}
