package artoria.data.dict;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;
import java.util.List;

/**
 * The data dictionary tools.
 * @author Kahle
 */
public class DictUtils {
    private static final Logger log = LoggerFactory.getLogger(DictUtils.class);
    private static volatile DictProvider dictProvider;

    public static DictProvider getDictProvider() {
        if (dictProvider != null) { return dictProvider; }
        synchronized (DictUtils.class) {
            if (dictProvider != null) { return dictProvider; }
            DictUtils.setDictProvider(new SimpleDictProvider());
            return dictProvider;
        }
    }

    public static void setDictProvider(DictProvider dictProvider) {
        Assert.notNull(dictProvider, "Parameter \"dictProvider\" must not null. ");
        log.info("Set dict provider: {}", dictProvider.getClass().getName());
        DictUtils.dictProvider = dictProvider;
    }

    public static void sync(Object strategy, Object data) {

        getDictProvider().sync(strategy, data);
    }

    public static Dict getByName(String group, String name) {

        return getDictProvider().getByName(group, name);
    }

    public static Dict getByCode(String group, String code) {

        return getDictProvider().getByCode(group, code);
    }

    public static Dict getByValue(String group, String value) {

        return getDictProvider().getByValue(group, value);
    }

    public static Dict findOne(Object dictQuery) {

        return getDictProvider().findOne(dictQuery);
    }

    public static <T> List<T> findMultiple(Object dictQuery, Type type) {

        return getDictProvider().findMultiple(dictQuery, type);
    }

    @Deprecated
    public static <T> List<T> findList(Object dictQuery, Type type) {

        return getDictProvider().findMultiple(dictQuery, type);
    }

}
