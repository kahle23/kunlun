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

import java.util.regex.Pattern;

public enum DriverType {
    HTMLUNIT(        HtmlUnitDriver.class,                   Pattern.compile("(?i)htmlunit.*")),
    PHANTOMJS(       PhantomJSDriver.class,                  Pattern.compile("(?i)phantomjs.*")),
    CHROME(          ChromeDriver.class,                     Pattern.compile("(?i)chrome.*")),
    FIREFOX(         FirefoxDriver.class,                    Pattern.compile("(?i)firefox.*")),
    EDGE(            EdgeDriver.class,                       Pattern.compile("(?i)edge.*")),
    IE(              InternetExplorerDriver.class,           Pattern.compile("(?i)internetexplorer.*|(?i)ie.*")),
    OPERA(           OperaDriver.class,                      Pattern.compile("(?i)opera.*")),
    SAFARI(          SafariDriver.class,                     Pattern.compile("(?i)safari.*")),
    REMOTE(          RemoteWebDriver.class,                  Pattern.compile("(?i)remote.*")),
    ;

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
            if (p.matcher(name).matches()) return type;
        }
        return null;
    }

}
