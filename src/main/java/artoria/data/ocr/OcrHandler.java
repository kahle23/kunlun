package artoria.data.ocr;

import artoria.core.Handler;

import java.util.Map;

/**
 * The optical character recognition handler.
 * @author Kahle
 */
public interface OcrHandler extends Handler {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the ocr handler.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Handle the OCR.
     * @param type The ocr data type
     * @param data The ocr data
     * @param clazz The ocr result class
     * @param features The ocr features
     * @return The ocr result or null
     */
    <T> T handle(Object type, Object data, Class<T> clazz, OcrFeature... features);

}
