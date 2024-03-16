/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.render;

import kunlun.core.Collector;
import kunlun.core.Renderer;
import kunlun.generator.Generator;
import kunlun.io.FileLoader;

import java.util.List;
import java.util.Map;

/**
 * The renderer-based content generator.
 * Construct the generator context based on configuration, and perform content rendering (i.e. generation)
 *      according to the renderer, attributes, and template configurations in the context.
 *
 * @author Kahle
 */
public interface RenderGenerator extends Generator {
    /**
     * The start override tag key tail name.
     */
    String START_OVERRIDE_TAG_KEY_TAIL = "StartOverrideTag";
    /**
     * The end override tag key tail name.
     */
    String END_OVERRIDE_TAG_KEY_TAIL = "EndOverrideTag";
    /**
     * The skip existed key tail name.
     */
    String SKIP_EXISTED_KEY_TAIL = "SkipExisted";
    /**
     * The filename key tail name.
     */
    String FILENAME_KEY_TAIL = "Filename";


    /**
     * Generate content based on the input configuration.
     * @param config The input configuration
     * @return The generated result
     */
    Object generate(Config config);


    /**
     * The configuration class for content generator based on renderer.
     * @author Kahle
     */
    interface Config {

    }

    /**
     * The context for renderer-based content generator.
     * @author Kahle
     */
    interface Context extends kunlun.core.Context {

        /**
         * Get the renderer.
         * @return The renderer
         */
        Renderer getRenderer();

        /**
         * Get the file loader.
         * @return The file loader
         */
        FileLoader getFileLoader();

        /**
         * Get the log collector.
         * @return The log collector
         */
        Collector getLogCollector();

        /**
         * Get the names of resources to be rendered (like table names).
         * @return The names of resources
         */
        List<String> getResourceNames();

        /**
         * Get the attributes corresponding to the resource by its name (like table information).
         * @param resourceName The names of resource
         * @return The attributes corresponding to the resource
         */
        Map<String, Object> getAttributes(String resourceName);

        /**
         * Set the attributes corresponding to the resource based on the resource name.
         * @param resourceName The names of resource
         * @param attributes The attributes corresponding to the resource
         */
        void setAttributes(String resourceName, Map<String, Object> attributes);

        /**
         * Get the template configurations.
         * @return The template configurations
         */
        List<TemplateConfig> getTemplateConfigs();

        /**
         * Set the template configurations.
         * @param configs The template configurations
         */
        void setTemplateConfigs(List<TemplateConfig> configs);

        /**
         * Get the final operation result according to the resource name.
         * @param resourceName The names of resource
         * @return The final operational result
         */
        Object getResult(String resourceName);

        /**
         * Set the final operation result according to the resource name.
         * @param resourceName The names of resource
         * @param result The final operational result
         */
        void setResult(String resourceName, Object result);

    }

    /**
     * The template configuration for content generator based on renderer.
     * @author Kahle
     */
    interface TemplateConfig {

        /**
         * Get the template name.
         * @return The template name
         */
        String getTemplateName();

        /**
         * Get the charset of the template.
         * @return The charset of the template
         */
        String getTemplateCharset();

        /**
         * Get the path of the template.
         * @return The path of the template
         */
        String getTemplatePath();

        /**
         * Get the content of the template.
         * @return The content of the template
         */
        String getTemplateContent();

        /**
         * Set the content of the template.
         * @param templateContent The content of the template
         */
        void setTemplateContent(String templateContent);

        /**
         * Get the charset of the output file.
         * @return The charset of the output file
         */
        String getOutputCharset();

        /**
         * Get the path of the output file.
         * @return The path of the output file
         */
        String getOutputPath();

        /**
         * Get the file suffix of the output file.
         * @return The file suffix of the output file
         */
        String getFileSuffix();

    }

}
