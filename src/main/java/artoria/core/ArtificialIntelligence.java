package artoria.core;

import java.util.Map;

/**
 * Provide the highest level of abstraction for AI (Artificial Intelligence).
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
 * @see <a href="https://en.wikipedia.org/wiki/Artificial_intelligence">Artificial Intelligence</a>
 * @author Kahle
 */
public interface ArtificialIntelligence {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the artificial intelligence.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * The artificial intelligence performs reasoning operations.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param arguments The arguments for inference calculations
     * @return The result of the inference calculation
     */
    Object execute(Object[] arguments);

}
