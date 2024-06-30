/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai;

import kunlun.core.ArtificialIntelligence;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.lang.reflect.Type;

import static kunlun.common.constant.Symbols.EMPTY_STRING;

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
public class AIUtils {
    private static final Logger log = LoggerFactory.getLogger(AIUtils.class);
    private static volatile AIProvider aiProvider;

    public static AIProvider getAIProvider() {
        if (aiProvider != null) { return aiProvider; }
        synchronized (AIUtils.class) {
            if (aiProvider != null) { return aiProvider; }
            AIUtils.setAIProvider(new SimpleAIProvider());
            return aiProvider;
        }
    }

    public static void setAIProvider(AIProvider aiProvider) {
        Assert.notNull(aiProvider, "Parameter \"aiProvider\" must not null. ");
        log.info("Set AI provider: {}", aiProvider.getClass().getName());
        AIUtils.aiProvider = aiProvider;
    }

    public static void registerHandler(String handlerName, ArtificialIntelligence aiHandler) {

        getAIProvider().registerHandler(handlerName, aiHandler);
    }

    public static void deregisterHandler(String handlerName) {

        getAIProvider().deregisterHandler(handlerName);
    }

    public static ArtificialIntelligence getHandler(String handlerName) {

        return getAIProvider().getHandler(handlerName);
    }

    public static Object execute(String handlerName, Object[] arguments) {

        return getAIProvider().execute(handlerName, arguments);
    }

    public static <T> T execute(String handlerName, Object input, String operation, Type type) {

        return getAIProvider().execute(handlerName, input, operation, type);
    }

    public static <T> T execute(String handlerName, Object input, Type type) {

        return getAIProvider().execute(handlerName, input, EMPTY_STRING, type);
    }

}
