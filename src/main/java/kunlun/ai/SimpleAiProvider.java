/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai;

import kunlun.core.ArtificialIntelligence;

import java.util.Map;

/**
 * The simple artificial intelligence provider.
 * @author Kahle
 */
public class SimpleAIProvider extends AbstractAIProvider {

    protected SimpleAIProvider(Map<String, Object> commonProperties,
                               Map<String, ArtificialIntelligence> aiEngines) {

        super(commonProperties, aiEngines);
    }

    public SimpleAIProvider() {

    }

}
