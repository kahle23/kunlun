/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.polyglot;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.polyglot.support.ScriptEngineService;
import kunlun.util.Assert;

/**
 * The polyglot execution tools.
 * @author Kahle
 */
public class PolyglotUtils {
    private static final Logger log = LoggerFactory.getLogger(PolyglotUtils.class);
    private static volatile PolyglotService polyglotService;

    public static PolyglotService getPolyglotService() {
        if (polyglotService != null) { return polyglotService; }
        synchronized (PolyglotUtils.class) {
            if (polyglotService != null) { return polyglotService; }
            PolyglotUtils.setPolyglotService(new ScriptEngineService());
            return polyglotService;
        }
    }

    public static void setPolyglotService(PolyglotService polyglotService) {
        Assert.notNull(polyglotService, "Parameter \"polyglotProvider\" must not null. ");
        log.info("Set polyglot provider: {}", polyglotService.getClass().getName());
        PolyglotUtils.polyglotService = polyglotService;
    }

    public static Object eval(String name, Object script, Object data) {

        return getPolyglotService().eval(name, script, null, data);
    }

    public static Object invoke(String name, Object script, String function, Object... arguments) {

        return getPolyglotService().invoke(name, script, null, function, arguments);
    }

    public static Object eval(String name, Object script, Object config, Object data) {

        return getPolyglotService().eval(name, script, config, data);
    }

    public static Object invoke(String name, Object script, Object config, String function, Object... arguments) {

        return getPolyglotService().invoke(name, script, config, function, arguments);
    }

}
