package artoria.ai;

import artoria.core.ArtificialIntelligence;

import java.util.Map;

/**
 * The simple AI engine provider.
 * @author Kahle
 */
public class SimpleAiProvider extends AbstractAiProvider {

    protected SimpleAiProvider(Map<String, Object> commonProperties,
                               Map<String, ArtificialIntelligence> aiEngines) {

        super(commonProperties, aiEngines);
    }

    public SimpleAiProvider() {

    }

}
