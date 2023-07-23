package artoria.ai;

import artoria.core.ArtificialIntelligence;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * The artificial intelligence tools.
 *
 * Artificial Intelligence (AI)
 * - Machine Learning (ML)
 *      - Deep Learning (DL)  [Artificial neural network (ANNs)]
 *          - Generative pre-trained transformers (GPT)
 *          - Large Language Model (LLM)
 * - Natural Language Processing (NLP)
 * - Machine perception
 *      - Optical character recognition (OCR)
 *
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

    public static void registerHandler(String handlerName, ArtificialIntelligence aiHandler) {

        getAiProvider().registerHandler(handlerName, aiHandler);
    }

    public static void deregisterHandler(String handlerName) {

        getAiProvider().deregisterHandler(handlerName);
    }

    public static ArtificialIntelligence getHandler(String handlerName) {

        return getAiProvider().getHandler(handlerName);
    }

    public static <T> T execute(String handlerName, Object input, String operation, Type type) {

        return getAiProvider().execute(handlerName, input, operation, type);
    }

    public static <T> T execute(String handlerName, Object input, Type type) {

        return getAiProvider().execute(handlerName, input, EMPTY_STRING, type);
    }

    public static Object execute(String handlerName, Object[] arguments) {

        return getAiProvider().execute(handlerName, arguments);
    }

}
