package kunlun.generator.render.support;

import kunlun.core.Collector;
import kunlun.core.Renderer;
import kunlun.data.Dict;
import kunlun.exception.ExceptionUtils;
import kunlun.generator.render.AbstractRenderGenerator;
import kunlun.io.FileLoader;
import kunlun.io.util.FileUtils;
import kunlun.io.util.FilenameUtils;
import kunlun.io.util.StringBuilderWriter;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.time.DateUtils;
import kunlun.util.Assert;
import kunlun.util.CloseUtils;

import java.io.*;
import java.util.Map;

import static kunlun.common.constant.Symbols.SLASH;
import static kunlun.common.constant.TimePatterns.NORM_DATETIME;
import static kunlun.common.constant.TimePatterns.Y4MD2MI;

/**
 * The abstract renderer-based content generator (generate to file).
 * @author Kahle
 */
public abstract class AbstractRenderFileGenerator extends AbstractRenderGenerator {
    private static final Logger log = LoggerFactory.getLogger(AbstractRenderFileGenerator.class);

    /**
     * Create directory.
     * @param parent The parent directory
     * @param child The child directory
     * @return The created directory
     */
    protected File mkdirs(String parent, String child) {

        return mkdirs(new File(parent), child);
    }

    /**
     * Create directory.
     * @param parent The parent directory
     * @param child The child directory
     * @return The created directory
     */
    protected File mkdirs(File parent, String child) {
        Assert.notNull(parent, "Parameter \"parent\" must not null. ");
        Assert.notBlank(child, "Parameter \"child\" must not blank. ");
        File newFile = new File(parent, child);
        mkdirs(newFile);
        return newFile;
    }

    /**
     * Create directory.
     * @param file The directory to be created
     */
    protected void mkdirs(File file) {
        Assert.notNull(file, "Parameter \"parent\" must not null. ");
        if (file.exists() && file.isFile()) {
            throw new IllegalStateException("Path \"" + file + "\" already exists and is a file! ");
        }
        if (!file.exists() && !file.mkdirs()) {
            throw new IllegalStateException("Failed to create directory \"" + file + "\"! ");
        }
    }

    /**
     * Do generate content based on the input configuration (with throws).
     * @param context The context of the generator
     * @param resName The resource name at generation time
     * @param config The template configuration for the generator
     */
    private void doGenerateThrows(Context context, String resName, TemplateConfig config) throws IOException {
        // Get variables and validate.
        Map<String, Object> attributes = context.getAttributes(resName);
        Collector  logCollector = context.getLogCollector();
        FileLoader fileLoader = context.getFileLoader();
        Renderer   renderer = context.getRenderer();
        String templateName = config.getTemplateName();
        String outputCharset = config.getOutputCharset();
        String outputPath = config.getOutputPath();
        String fileSuffix = config.getFileSuffix();
        String filename = (String) attributes.get(templateName + FILENAME_KEY_TAIL);
        String startOverrideTag = (String) attributes.get(templateName + START_OVERRIDE_TAG_KEY_TAIL);
        String endOverrideTag = (String) attributes.get(templateName + END_OVERRIDE_TAG_KEY_TAIL);
        Boolean skipExisted = (Boolean) attributes.get(templateName + SKIP_EXISTED_KEY_TAIL);
        // Validate.
        Assert.notNull(logCollector, "Variable \"logCollector\" must not null. ");
        Assert.notNull(fileLoader, "Variable \"fileLoader\" must not null. ");
        Assert.notNull(renderer, "Variable \"renderer\" must not null. ");
        Assert.notBlank(outputCharset, "Variable \"outputCharset\" must not blank. ");
        Assert.notBlank(outputPath, "Variable \"outputPath\" must not blank. ");
        Assert.notBlank(fileSuffix, "Variable \"fileSuffix\" must not blank. ");
        Assert.notBlank(filename, "Variable \"filename\" must not blank. ");
        // Get output directory.
        // Regarding the conversion of "/" and "\", it cannot rely on "File", it only takes effect on Windows.
        File outputFile = new File(
                FilenameUtils.normalize(outputPath + SLASH + filename + withLeftDot(fileSuffix)));
        mkdirs(outputFile.getParentFile());
        // Get template content.
        String templateContent = getTemplateContent(logCollector, fileLoader, config);
        // Create template filled model.
        Dict model = Dict.of(attributes);
        model.set("generationTime", DateUtils.format(NORM_DATETIME));
        model.set("nowDate", DateUtils.format(Y4MD2MI));
        // Print log.
        String logStr = String.format(
                "Template name \"%s\": rendering the file corresponding to resource name \"%s\". "
                , templateName, resName);
        log.info(logStr); logCollector.collect(logStr);
        // Handle whether existing.
        if (outputFile.exists()) {
            if (skipExisted != null && skipExisted) { return; }
            logStr = String.format(
                    "The file \"%s\" already exists, it will be try replace. ", outputFile.getName());
            log.info(logStr); logCollector.collect(logStr);
            // Generated content.
            Writer builderWriter = new StringBuilderWriter();
            renderer.render(templateContent, outputFile.getName(), model, builderWriter);
            String generation = builderWriter.toString();
            // Read file content.
            byte[] fileBytes = FileUtils.read(outputFile);
            String fileContent = new String(fileBytes, outputCharset);
            // Do replace.
            String outputStr = replaceContent(
                    logCollector, generation, fileContent, startOverrideTag, endOverrideTag);
            // Write to file.
            if (outputStr == null) { return; }
            byte[] outputBytes = outputStr.getBytes(outputCharset);
            FileUtils.write(outputBytes, outputFile);
        }
        else {
            // Try to create new file.
            if (!outputFile.createNewFile()) {
                throw new IOException("File \"" + outputFile + "\" create failure. ");
            }
            // Write to file.
            Writer writer = null;
            try {
                OutputStream output = new FileOutputStream(outputFile);
                writer = new OutputStreamWriter(output, outputCharset);
                renderer.render(templateContent, outputFile.getName(), model, writer);
            }
            finally { CloseUtils.closeQuietly(writer); }
        }
    }

    @Override
    protected void doGenerate(Context context, String resourceName, TemplateConfig config) {
        try { doGenerateThrows(context, resourceName, config); }
        catch (IOException e) { throw ExceptionUtils.wrap(e); }
    }

}
