/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util.handler;

import kunlun.data.validation.support.ValidationConfig;

import java.util.Collection;

/**
 * The script processing tool (used for custom logic).
 * @author Kahle
 */
public interface ScriptHandler {

    /**
     * Eval the script by script name.
     * @param scriptName The script engine name
     * @param script The script
     * @param data The data
     * @return The eval result
     */
    Object eval(String scriptName, String script, Object data);

    /**
     * Validate data by validation configs.
     * @param scriptName The script engine name
     * @param configs The validation configs
     * @param data The data
     */
    void validate(String scriptName, Collection<ValidationConfig> configs, Object data);

    /**
     * Throw exception if validate failure.
     * @param validate The validate result
     * @param message The failure message
     */
    void throwValidationException(boolean validate, String message);

}
