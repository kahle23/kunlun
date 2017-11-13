package saber.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * @author Kahle
 */
public enum DriverType {

    /**
     * HtmlUnit Driver
     */
    HTMLUNIT(        HtmlUnitDriver.class,                   Pattern.compile("(?i).*htmlunit.*")),

    /**
     * PhantomJS Driver
     */
    PHANTOMJS(       PhantomJSDriver.class,                  Pattern.compile("(?i).*phantomjs.*")),

    /**
     * Chrome Driver
     */
    CHROME(          ChromeDriver.class,                     Pattern.compile("(?i).*chrome.*")),

    /**
     * Firefox Driver
     */
    FIREFOX(         FirefoxDriver.class,                    Pattern.compile("(?i).*firefox.*")),

    /**
     * Edge Driver
     */
    EDGE(            EdgeDriver.class,                       Pattern.compile("(?i).*edge.*")),

    /**
     * Internet Explorer Driver
     */
    IE(              InternetExplorerDriver.class,           Pattern.compile("(?i).*internetexplorer.*|(?i).*ie.*")),

    /**
     * Opera Driver
     */
    OPERA(           OperaDriver.class,                      Pattern.compile("(?i).*opera.*")),

    /**
     * Safari Driver
     */
    SAFARI(          SafariDriver.class,                     Pattern.compile("(?i).*safari.*")),

    /**
     * Remote Web Driver
     */
    REMOTE(          RemoteWebDriver.class,                  Pattern.compile("(?i).*remote.*")),

    ;

    private final static Logger log = LoggerFactory.getLogger(DriverType.class);
    private Class<? extends WebDriver> driverClass;
    private Pattern driverName;

    DriverType(Class<? extends WebDriver> driverClass, Pattern driverName) {
        this.driverName = driverName;
        this.driverClass = driverClass;
    }

    public Class<? extends WebDriver> getDriverClass() {
        return driverClass;
    }

    public Pattern getDriverName() {
        return driverName;
    }

    @Override
    public String toString() {
        return driverClass.getSimpleName();
    }

    public static DriverType find(String name) {
        DriverType[] types = DriverType.values();
        for (DriverType type : types) {
            Pattern p = type.getDriverName();
            if (p.matcher(name).matches()) {
                log.info("Find " + type.getDriverClass().getSimpleName() + ". ");
                return type;
            }
        }
        log.info("Find nothing. ");
        return null;
    }

}
