package artoria.core.handler;

import artoria.core.Handler;

/**
 * The configuration supported handler.
 * @author Kahle
 */
public interface ConfigSupportHandler extends Handler {

    /**
     * Get the handler config.
     * @return The handler config
     */
    HandlerConfig getConfig();

    /**
     * The handler configuration.
     * @author Kahle
     */
    interface HandlerConfig {

        /**
         * Return the config value associated with the given name.
         * @param name The config name
         * @return The config value
         */
        String getProperty(String name);

    }

}
