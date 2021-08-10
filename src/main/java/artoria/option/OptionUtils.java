package artoria.option;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Map;

import static artoria.common.Constants.*;

public class OptionUtils {
    private static Logger log = LoggerFactory.getLogger(OptionUtils.class);
    private static OptionProvider optionProvider;

    public static OptionProvider getOptionProvider() {
        if (optionProvider != null) { return optionProvider; }
        synchronized (OptionUtils.class) {
            if (optionProvider != null) { return optionProvider; }
            OptionUtils.setOptionProvider(new SimpleOptionProvider());
            return optionProvider;
        }
    }

    public static void setOptionProvider(OptionProvider optionProvider) {
        Assert.notNull(optionProvider, "Parameter \"optionProvider\" must not null. ");
        log.info("Set option provider: {}", optionProvider.getClass().getName());
        OptionUtils.optionProvider = optionProvider;
    }

    public static String getString(String name) {

        return getOption(EMPTY_STRING, name, String.class, NULL_STR);
    }

    // There is no "getString(String name, String defaultValue)".

    public static String getString(String owner, String name) {

        return getOption(owner, name, String.class, NULL_STR);
    }

    public static String getString(String owner, String name, String defaultValue) {

        return getOption(owner, name, String.class, defaultValue);
    }

    public static boolean getBoolean(String name) {

        return getOption(EMPTY_STRING, name, Boolean.class, false);
    }

    public static boolean getBoolean(String name, boolean defaultValue) {

        return getOption(EMPTY_STRING, name, Boolean.class, defaultValue);
    }

    public static boolean getBoolean(String owner, String name) {

        return getOption(owner, name, Boolean.class, false);
    }

    public static boolean getBoolean(String owner, String name, boolean defaultValue) {

        return getOption(owner, name, Boolean.class, defaultValue);
    }

    public static int getInteger(String name) {

        return getOption(EMPTY_STRING, name, Integer.class, ZERO);
    }

    public static int getInteger(String name, int defaultValue) {

        return getOption(EMPTY_STRING, name, Integer.class, defaultValue);
    }

    public static int getInteger(String owner, String name) {

        return getOption(owner, name, Integer.class, ZERO);
    }

    public static int getInteger(String owner, String name, int defaultValue) {

        return getOption(owner, name, Integer.class, defaultValue);
    }

    public static <T> T getRequiredOption(String name, Class<T> type) {

        return getRequiredOption(EMPTY_STRING, name, type);
    }

    public static <T> T getRequiredOption(String owner, String name, Class<T> type) {

        return getOptionProvider().getRequiredOption(owner, name, type);
    }

    public static <T> T getOption(String name, Class<T> type) {

        return getOption(EMPTY_STRING, name, type, null);
    }

    public static <T> T getOption(String name, Class<T> type, T defaultValue) {

        return getOption(EMPTY_STRING, name, type, defaultValue);
    }

    public static <T> T getOption(String owner, String name, Class<T> type) {

        return getOption(owner, name, type, null);
    }

    public static <T> T getOption(String owner, String name, Class<T> type, T defaultValue) {

        return getOptionProvider().getOption(owner, name, type, defaultValue);
    }

    public static boolean containsOption(String name) {

        return containsOption(EMPTY_STRING, name);
    }

    public static boolean containsOption(String owner, String name) {

        return getOptionProvider().containsOption(owner, name);
    }

    public static Map<String, Object> getOptions(String owner) {

        return getOptionProvider().getOptions(owner);
    }

    public static Object getOption(String name) {

        return getOption(EMPTY_STRING, name, NULL_OBJ);
    }

    public static Object getOption(String name, Object defaultValue) {

        return getOption(EMPTY_STRING, name, defaultValue);
    }

    public static Object getOption(String owner, String name) {

        return getOption(owner, name, NULL_OBJ);
    }

    public static Object getOption(String owner, String name, Object defaultValue) {

        return getOptionProvider().getOption(owner, name, defaultValue);
    }

    public static Object setOption(String name, Object value) {

        return setOption(EMPTY_STRING, name, value);
    }

    public static Object setOption(String owner, String name, Object value) {

        return getOptionProvider().setOption(owner, name, value);
    }

    public static Object removeOption(String name) {

        return removeOption(EMPTY_STRING, name);
    }

    public static Object removeOption(String owner, String name) {

        return getOptionProvider().removeOption(owner, name);
    }

}
