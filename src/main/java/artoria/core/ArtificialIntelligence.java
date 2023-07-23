package artoria.core;

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
