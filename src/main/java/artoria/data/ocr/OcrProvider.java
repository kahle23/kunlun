package artoria.data.ocr;

import java.util.Map;

/**
 * The optical character recognition provider.
 * @see <a href="https://en.wikipedia.org/wiki/Optical_character_recognition">Optical character recognition</a>
 * @author Kahle
 */
public interface OcrProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the ocr handler.
     * @param name The ocr handler name
     * @param ocrHandler The ocr handler
     */
    void registerHandler(String name, OcrHandler ocrHandler);

    /**
     * Deregister the ocr handler.
     * @param name The ocr handler name
     */
    void deregisterHandler(String name);

    /**
     * Get the ocr handler by name.
     * @param name The ocr handler name
     * @return The ocr handler
     */
    OcrHandler getOcrHandler(String name);

    /**
     * Handle the OCR.
     * @param name The ocr handler name
     * @param type The ocr data type
     * @param data The ocr data
     * @param clazz The ocr result class
     * @param features The ocr features
     * @return The ocr result or null
     */
    <T> T handle(String name, Object type, Object data, Class<T> clazz, OcrFeature... features);

}
