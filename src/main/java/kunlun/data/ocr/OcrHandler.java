/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.ocr;

import kunlun.core.Handler;

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
     * @param arguments The ocr arguments (maybe is configuration or feature)
     * @return The ocr result or null
     */
    <T> T handle(Object type, Object data, Class<T> clazz, Object... arguments);

}
