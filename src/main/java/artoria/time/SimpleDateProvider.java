package artoria.time;

import artoria.collect.ReferenceMap;
import artoria.lang.ReferenceType;
import artoria.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * The date formatter and parser simple implement by jdk.
 * @author Kahle
 */
public class SimpleDateProvider implements DateProvider {
    private ThreadLocal<Map<String, SimpleDateFormat>> dateFormatCache = new ThreadLocal<Map<String, SimpleDateFormat>>();

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

    @Override
    public String format(Date date, String pattern) {
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        return getDateFormat(pattern).format(date);
    }

    @Override
    public Date parse(String dateString, String pattern) throws ParseException {
        Assert.notBlank(dateString, "Parameter \"dateString\" must not blank. ");
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        return getDateFormat(pattern).parse(dateString);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (dateFormatCache != null) {
            dateFormatCache.remove();
        }
    }

}
