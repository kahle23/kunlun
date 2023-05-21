package artoria.polyglot;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.polyglot.support.ScriptEngineProvider;
import artoria.util.Assert;

/**
 * The polyglot execution tools.
 * @author Kahle
 */
public class PolyglotUtils {
    private static final Logger log = LoggerFactory.getLogger(PolyglotUtils.class);
    private static volatile PolyglotProvider polyglotProvider;

    public static PolyglotProvider getPolyglotProvider() {
        if (polyglotProvider != null) { return polyglotProvider; }
        synchronized (PolyglotUtils.class) {
            if (polyglotProvider != null) { return polyglotProvider; }
            PolyglotUtils.setPolyglotProvider(new ScriptEngineProvider());
            return polyglotProvider;
        }
    }

    public static void setPolyglotProvider(PolyglotProvider polyglotProvider) {
        Assert.notNull(polyglotProvider, "Parameter \"polyglotProvider\" must not null. ");
        log.info("Set polyglot provider: {}", polyglotProvider.getClass().getName());
        PolyglotUtils.polyglotProvider = polyglotProvider;
    }

    public static Object eval(String name, Object script, Object data) {

        return getPolyglotProvider().eval(name, script, null, data);
    }

    public static Object invoke(String name, Object script, String function, Object... arguments) {

        return getPolyglotProvider().invoke(name, script, null, function, arguments);
    }

    public static Object eval(String name, Object script, Object config, Object data) {

        return getPolyglotProvider().eval(name, script, config, data);
    }

    public static Object invoke(String name, Object script, Object config, String function, Object... arguments) {

        return getPolyglotProvider().invoke(name, script, config, function, arguments);
    }

}
