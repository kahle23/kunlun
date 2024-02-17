/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.time;

import kunlun.data.ReferenceType;
import kunlun.data.collect.ReferenceMap;
import kunlun.exception.ExceptionUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static kunlun.common.constant.TimePatterns.*;

/**
 * The date formatter and parser simple implement by jdk.
 * @author Kahle
 */
public class SimpleDateProvider implements DateProvider {
    private final ThreadLocal<Map<String, SimpleDateFormat>> dateFormatCache = new ThreadLocal<Map<String, SimpleDateFormat>>();
    private final Set<String> datePatterns = new HashSet<String>();
    private final String defaultPattern;
    private static final Logger log = LoggerFactory.getLogger(SimpleDateProvider.class);

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

        this(NORM_DATETIME);
    }

    public SimpleDateProvider(String defaultPattern) {
        Assert.notBlank(defaultPattern, "Parameter \"defaultPattern\" must not blank. ");
        register(this.defaultPattern = defaultPattern);
        register("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        register("yyyy-MM-dd'T'HH:mm:ssZ");
        register(UTC_MS_NOZ);
        register(UTC_MS);
        register(UTC_NOZ);
        register(UTC);
        register(Y4MD2MI_HMS2CO_S3);
        register(Y4MD2MI_HMS2CO);
        register(Y4MD2MI_HM2CO);
        register(Y4MD2MI);
        register(Y4MD2SL_HMS2CO_S3);
        register(Y4MD2SL_HMS2CO);
        register(Y4MD2SL_HM2CO);
        register(Y4MD2SL);
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
