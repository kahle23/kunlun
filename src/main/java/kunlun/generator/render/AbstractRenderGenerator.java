/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.render;

import kunlun.core.Renderer;
import kunlun.exception.ExceptionUtils;
import kunlun.io.FileLoader;
import kunlun.io.util.IOUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.CloseUtils;
import kunlun.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static kunlun.common.constant.Charsets.STR_UTF_8;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.DOT;
import static kunlun.common.constant.Symbols.EMPTY_STRING;
import static kunlun.common.constant.Words.SUCCESS;
import static kunlun.io.util.IOUtils.EOF;

/**
 * The abstract renderer-based content generator.
 * @author Kahle
 */
public abstract class AbstractRenderGenerator implements RenderGenerator {
    private static final Logger log = LoggerFactory.getLogger(AbstractRenderGenerator.class);

    /**
     * Get the template content according to the template configuration and file loader.
     *
     * If the content in the template configuration is not blank, return directly.
     * If it is empty, load the template content according to the template path
     *      and set it in the template configuration.
     *
     * @param logCollector The log collector
     * @param fileLoader The file loader
     * @param config The template configuration
     * @return The template content
     */
    protected String getTemplateContent(StringBuilder logCollector,
                                        FileLoader fileLoader,
                                        TemplateConfig config) {
        // Try get template content.
        String content = config.getTemplateContent();
        if (StringUtils.isNotBlank(content)) { return content; }
        // Load template content by path.
        String templatePath = config.getTemplatePath();
        String charset = config.getTemplateCharset();
        InputStream in = null;
        // Do load.
        try {
            in = fileLoader.load(templatePath);
            config.setTemplateContent(IOUtils.toString(in, charset));
            return config.getTemplateContent();
        }
        catch (IOException e) { throw ExceptionUtils.wrap(e); }
        finally { CloseUtils.closeQuietly(in); }
    }

    /**
     * Based on the override tag, find the parts that can be covered from the old and new content,
     *      and replace the old with the new.
     * @param logCollector The log collector
     * @param newContent The new content
     * @param oldContent The old content
     * @param startOverrideTag The start override tag
     * @param endOverrideTag The end override tag
     * @return The replaced content
     */
    protected String replaceContent(StringBuilder logCollector,
                                    String newContent,
                                    String oldContent,
                                    String startOverrideTag,
                                    String endOverrideTag) {
        Assert.notBlank(newContent, "Parameter \"newContent\" must not blank. ");
        if (StringUtils.isBlank(startOverrideTag)) { return newContent; }
        if (StringUtils.isBlank(endOverrideTag)) { return newContent; }
        if (StringUtils.isBlank(oldContent)) { return newContent; }
        // Variable definition.
        int oldIndex = ZERO, newIndex = ZERO, count = ZERO, tmpFromIndex;
        StringBuilder result = new StringBuilder(); String logStr;
        do {
            // Look for the start override tag in the old content.
            int oldStart = oldContent.indexOf(startOverrideTag, oldIndex);
            // If it didn't find it once.
            if (oldStart == EOF && count == ZERO) {
                logStr = "Can not find start override tag in old content. ";
                log.info(logStr); logCollector.append(logStr);
                return null;
            }
            // No start override tag found, indicating that processing is complete.
            if (oldStart == EOF) {
                int oldEnd = oldContent.indexOf(endOverrideTag, oldIndex);
                if (oldEnd != EOF) {
                    logStr = String.format(
                            "The end override tag should not exist after index %s in old content. ", oldIndex);
                    log.info(logStr); logCollector.append(logStr);
                    return null;
                }
                int newStart = newContent.indexOf(startOverrideTag, newIndex);
                if (newStart != EOF) {
                    logStr = String.format(
                            "The start override tag should not exist after index %s in new content. ", newIndex);
                    log.info(logStr); logCollector.append(logStr);
                    return null;
                }
                int newEnd = newContent.indexOf(endOverrideTag, newIndex);
                if (newEnd != EOF) {
                    logStr = String.format(
                            "The end override tag should not exist after index %s in new content. ", newIndex);
                    log.info(logStr); logCollector.append(logStr);
                    return null;
                }
                result.append(oldContent, oldIndex, oldContent.length());
                return result.toString();
            }
            // Look for the end override tag in the old content.
            tmpFromIndex = oldStart + startOverrideTag.length();
            int oldEnd = oldContent.indexOf(endOverrideTag, tmpFromIndex);
            if (oldEnd == EOF) {
                logStr = "Can not find end override tag in old content. ";
                log.info(logStr); logCollector.append(logStr);
                return null;
            }
            // Look for the start override tag in the new content.
            int newStart = newContent.indexOf(startOverrideTag, newIndex);
            if (newStart == EOF) {
                logStr = "Can not find start override tag in new content. ";
                log.info(logStr); logCollector.append(logStr);
                return null;
            }
            // Look for the end override tag in the new content.
            tmpFromIndex = newStart + startOverrideTag.length();
            int newEnd = newContent.indexOf(endOverrideTag, tmpFromIndex);
            if (newEnd == EOF) {
                logStr = "Can not find end override tag in new content. ";
                log.info(logStr); logCollector.append(logStr);
                return null;
            }
            // The assembly results.
            result.append(oldContent, oldIndex, oldStart);
            result.append(newContent, newStart, newEnd);
            result.append(endOverrideTag);
            oldIndex = oldEnd + endOverrideTag.length();
            newIndex = newEnd + endOverrideTag.length();
            count++;
        } while (true);
    }

    /**
     * Put a dot on the left side of the string.
     * @param str The string to be processed
     * @return The processed string
     */
    protected String withLeftDot(String str) {
        if (StringUtils.isBlank(str)) { return EMPTY_STRING; }
        if (str.startsWith(DOT)) { return str; }
        return DOT + str;
    }

    /**
     * Get the operation result according to the configuration and context.
     * @param config The input configuration
     * @param context The context of the generator
     * @return The operation result
     */
    protected Object getResult(Config config, Context context) {

        return SUCCESS;
    }

    /**
     * Do generate content based on the input configuration.
     * @param context The context of the generator
     * @param resourceName The resource name at generation time
     * @param config The template configuration for the generator
     */
    protected abstract void doGenerate(Context context, String resourceName, TemplateConfig config);

    /**
     * Build the context object based on the input configuration.
     * @param config The input configuration
     * @return The built context object
     */
    protected abstract Context buildContext(Config config);

    @Override
    public Object generate(Config config) {
        // Validate config and build context.
        Assert.notNull(config, "Parameter \"config\" must not null. ");
        Context context = buildContext(config);
        Assert.notNull(context, "The context build failure. ");
        Assert.notNull(context.getLogCollector()
                , "The log collector for context cannot be null. ");
        // Get resource names and template configs.
        List<TemplateConfig> templateConfigs = context.getTemplateConfigs();
        List<String> resourceNames = context.getResourceNames();
        // Loop resource names.
        for (String resourceName : resourceNames) {
            // Validate.
            Assert.notBlank(resourceName, "Variable \"resourceName\" must not blank. ");
            Assert.notNull(context.getAttributes(resourceName)
                    , "Variable \"context.attributes\" must not null. ");
            // Loop template configs.
            for (TemplateConfig templateConfig : templateConfigs) {
                // Validate and do generate.
                Assert.notNull(templateConfig, "Variable \"templateConfig\" must not null. ");
                Assert.notBlank(templateConfig.getTemplateName()
                        , "Variable \"templateConfig.templateName\" must not blank. ");
                doGenerate(context, resourceName, templateConfig);
            }
        }
        // Finish.
        return getResult(config, context);
    }

    /**
     * The simple implementation of the context object for content generator.
     * @author Kahle
     */
    public static class ContextImpl implements Context {
        private final Map<String, Map<String, Object>> attributesMap = new LinkedHashMap<String, Map<String, Object>>();
        private final Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        private final StringBuilder  logCollector = new StringBuilder();
        private List<TemplateConfig> templateConfigs;
        private List<String> resourceNames;
        private FileLoader fileLoader;
        private Renderer renderer;

        @Override
        public Renderer getRenderer() {

            return renderer;
        }

        public void setRenderer(Renderer renderer) {

            this.renderer = renderer;
        }

        @Override
        public FileLoader getFileLoader() {

            return fileLoader;
        }

        public void setFileLoader(FileLoader fileLoader) {

            this.fileLoader = fileLoader;
        }

        @Override
        public StringBuilder getLogCollector() {

            return logCollector;
        }

        @Override
        public List<String> getResourceNames() {

            return resourceNames;
        }

        public void setResourceNames(List<String> resourceNames) {

            this.resourceNames = resourceNames;
        }

        @Override
        public Map<String, Object> getAttributes(String resourceName) {

            return attributesMap.get(resourceName);
        }

        @Override
        public void setAttributes(String resourceName, Map<String, Object> attributes) {

            attributesMap.put(resourceName, attributes);
        }

        @Override
        public List<TemplateConfig> getTemplateConfigs() {

            return templateConfigs;
        }

        @Override
        public void setTemplateConfigs(List<TemplateConfig> templateConfigs) {

            this.templateConfigs = templateConfigs;
        }

        @Override
        public Object getResult(String resourceName) {

            return resultMap.get(resourceName);
        }

        @Override
        public void setResult(String resourceName, Object result) {

            resultMap.put(resourceName, result);
        }
    }

    /**
     * The simple implementation of the content generator's template configuration.
     * @author Kahle
     */
    public static class TemplateConfigImpl implements TemplateConfig {
        private final String templateName;
        private String templateCharset;
        private String templatePath;
        private String templateContent;
        private String outputCharset;
        private String outputPath;
        private String fileSuffix;

        public TemplateConfigImpl(String templateName) {
            Assert.notBlank(templateName, "Parameter \"templateName\" must not blank. ");
            this.templateName = templateName;
            this.templateCharset = STR_UTF_8;
            this.outputCharset = STR_UTF_8;
        }

        @Override
        public String getTemplateName() {

            return templateName;
        }

        @Override
        public String getTemplateCharset() {

            return templateCharset;
        }

        public void setTemplateCharset(String templateCharset) {

            this.templateCharset = templateCharset;
        }

        @Override
        public String getTemplatePath() {

            return templatePath;
        }

        public void setTemplatePath(String templatePath) {

            this.templatePath = templatePath;
        }

        @Override
        public String getTemplateContent() {

            return templateContent;
        }

        @Override
        public void setTemplateContent(String templateContent) {

            this.templateContent = templateContent;
        }

        @Override
        public String getOutputCharset() {

            return outputCharset;
        }

        public void setOutputCharset(String outputCharset) {

            this.outputCharset = outputCharset;
        }

        @Override
        public String getOutputPath() {

            return outputPath;
        }

        public void setOutputPath(String outputPath) {

            this.outputPath = outputPath;
        }

        @Override
        public String getFileSuffix() {

            return fileSuffix;
        }

        public void setFileSuffix(String fileSuffix) {

            this.fileSuffix = fileSuffix;
        }
    }

}
