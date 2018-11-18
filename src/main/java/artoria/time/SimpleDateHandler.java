package artoria.time;

import artoria.collection.ReferenceHashMap;
import artoria.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Date formater and parser simple implement by jdk.
 * @author Kahle
 */
public class SimpleDateHandler implements DateFormater, DateParser {
    private ThreadLocal<Map<String, SimpleDateFormat>> dateFormatCache = new ThreadLocal<Map<String, SimpleDateFormat>>();

    private SimpleDateFormat takeSimpleDateFormat(String pattern) {
        Map<String, SimpleDateFormat> cache = dateFormatCache.get();
        if (cache == null) {
            cache = new ReferenceHashMap<String, SimpleDateFormat>(ReferenceHashMap.Type.SOFT);
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
        Assert.notNull(date, "Parameter \"date\" must not null. ");
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        return this.takeSimpleDateFormat(pattern).format(date);
    }

    @Override
    public Date parse(String dateString, String pattern) throws ParseException {
        Assert.notBlank(dateString, "Parameter \"dateString\" must not blank. ");
        Assert.notBlank(pattern, "Parameter \"pattern\" must not blank. ");
        return this.takeSimpleDateFormat(pattern).parse(dateString);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dateFormatCache.remove();
    }

}
