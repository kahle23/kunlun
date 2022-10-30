package artoria.time;

import artoria.data.ReferenceType;
import artoria.data.collect.ReferenceMap;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static artoria.common.Constants.*;

/**
 * The date formatter and parser simple implement by jdk.
 * @author Kahle
 */
public class SimpleDateProvider implements DateProvider {
    private final ThreadLocal<Map<String, SimpleDateFormat>> dateFormatCache = new ThreadLocal<Map<String, SimpleDateFormat>>();
    private final Set<String> datePatterns = new HashSet<String>();
    private final String defaultPattern;
    private static Logger log = LoggerFactory.getLogger(SimpleDateProvider.class);

    private SimpleDateFormat getDateFormat(String pattern) {
        Map<String, SimpleDateFormat> cache = dateFormatCache.get();
        if (cache == null) {
            cache = new ReferenceMap<String, SimpleDateFormat>(ReferenceType.SOFT);
            dateFormatCache.set(cache);
        }
        SimpleDateFormat format = cache.get(pattern);
        if (format == null) {
            format = new SimpleDateFormat(pattern);
            cache.put(pattern, format);
        }
        return format;
    }

    public SimpleDateProvider() {

        this(NORM_DATETIME_PATTERN);
    }

    public SimpleDateProvider(String defaultPattern) {
        Assert.notBlank(defaultPattern, "Parameter \"defaultPattern\" must not blank. ");
        register(this.defaultPattern = defaultPattern);
        register(FULL_DATETIME_PATTERN);
        register(NORM_DATETIME_MS_PATTERN);
        register(NORM_DATETIME_PATTERN);
        register(UTC_SIMPLE_MS_PATTERN);
        register(UTC_MS_PATTERN);
        register("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        register("yyyy-MM-dd'T'HH:mm:ss'Z'");
        register("yyyy-MM-dd'T'HH:mm:ssZ");
        register("yyyy-MM-dd HH:mm:ss");
        register("yyyy-MM-dd HH:mm");
        register("yyyy-MM-dd");
        register("yyyy/MM/dd HH:mm:ss");
        register("yyyy/MM/dd HH:mm");
        register("yyyy/MM/dd");
    }

    @Override
    public String getDefaultPattern() {

        return defaultPattern;
    }

    @Override
    public void register(String pattern) {
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        datePatterns.add(pattern);
        log.info("Register the date pattern \"{}\" success. ", pattern);
    }

    @Override
    public void deregister(String pattern) {
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        datePatterns.remove(pattern);
        log.info("Deregister the date pattern \"{}\" success. ", pattern);
    }

    @Override
    public String format(Date date) {

        return format(date, getDefaultPattern());
    }

    @Override
    public String format(Date date, String pattern) {
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        return getDateFormat(pattern).format(date);
    }

    @Override
    public Date parse(String dateString) {
        if (StringUtils.isBlank(dateString)) { return null; }
        for (String datePattern : datePatterns) {
            try {
                return parse(dateString, datePattern);
            }
            catch (Exception ex) {
                log.debug(
                        "This is a mistake that should not be taken seriously. ",
                        ex
                );
            }
        }
        throw new UnsupportedOperationException(
                "All registered date pattern are not supported. "
        );
    }

    @Override
    public Date parse(String dateString, String pattern) {
        Assert.notBlank(dateString, "Parameter \"dateString\" must not blank. ");
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        try {
            return getDateFormat(pattern).parse(dateString);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
