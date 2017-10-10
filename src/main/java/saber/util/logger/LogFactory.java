package saber.util.logger;

public abstract class LogFactory {

    public static org.slf4j.Logger slf4j(Class<?> clazz) {
        return org.slf4j.LoggerFactory.getLogger(clazz);
    }

    public static org.slf4j.Logger slf4j(String name) {
        return org.slf4j.LoggerFactory.getLogger(name);
    }

    public static org.apache.commons.logging.Log commonsLog(Class<?> clazz) {
        return org.apache.commons.logging.LogFactory.getLog(clazz);
    }

    public static org.apache.commons.logging.Log commonsLog(String name) {
        return org.apache.commons.logging.LogFactory.getLog(name);
    }

    public static java.util.logging.Logger jdkLog(String name) {
        return java.util.logging.Logger.getLogger(name);
    }

    public static java.util.logging.Logger jdkLog(Class<?> clazz) {
        return java.util.logging.Logger.getLogger(clazz.getName());
    }

    public static org.apache.log4j.Logger log4j(Class<?> clazz) {
        return org.apache.log4j.Logger.getLogger(clazz);
    }

    public static org.apache.log4j.Logger log4j(String name) {
        return org.apache.log4j.Logger.getLogger(name);
    }

}
