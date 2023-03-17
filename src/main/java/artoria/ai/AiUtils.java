package artoria.ai;

import artoria.core.ArtificialIntelligence;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * The artificial intelligence tools.
 * @author Kahle
 */
public class AiUtils {
    private static final Logger log = LoggerFactory.getLogger(AiUtils.class);
    private static volatile AiProvider aiProvider;

    public static AiProvider getAiProvider() {
        if (aiProvider != null) { return aiProvider; }
        synchronized (AiUtils.class) {
            if (aiProvider != null) { return aiProvider; }
            AiUtils.setAiProvider(new SimpleAiProvider());
            return aiProvider;
        }
    }

    public static void setAiProvider(AiProvider aiProvider) {
        Assert.notNull(aiProvider, "Parameter \"aiProvider\" must not null. ");
        log.info("Set ai provider: {}", aiProvider.getClass().getName());
        AiUtils.aiProvider = aiProvider;
    }

    public static void registerHandler(String engineName, ArtificialIntelligence aiEngine) {

        getAiProvider().registerEngine(engineName, aiEngine);
    }

    public static void deregisterHandler(String engineName) {

        getAiProvider().deregisterEngine(engineName);
    }

    public static ArtificialIntelligence getEngine(String engineName) {

        return getAiProvider().getEngine(engineName);
    }

    public static <T> T execute(Object input, String engineName, String strategy, Type type) {

        return getAiProvider().execute(input, engineName, strategy, type);
    }

    public static <T> T execute(Object input, String engineName, Type type) {

        return getAiProvider().execute(input, engineName, EMPTY_STRING, type);
    }

    public static Object execute(String engineName, Object[] arguments) {

        return getAiProvider().execute(engineName, arguments);
    }

}
