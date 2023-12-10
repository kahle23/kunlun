package artoria.generator.code.support.java;

import artoria.core.Renderer;
import artoria.data.Dict;
import artoria.exception.ExceptionUtils;
import artoria.generator.code.AbstractFileBuilder;
import artoria.generator.code.FileContext;
import artoria.io.util.FileUtils;
import artoria.io.util.StringBuilderWriter;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.renderer.TextRenderer;
import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.CloseUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.FULL_DATETIME_PATTERN;

public class SimpleFileBuilder extends AbstractFileBuilder {
    private static final Logger log = LoggerFactory.getLogger(SimpleFileBuilder.class);

    public SimpleFileBuilder(String name, Renderer engine) {

        super(name, engine);
    }

    protected void build(FileContext context, String tableName) throws IOException {
        Assert.notNull(context, "Parameter \"context\" must not null. ");
        Renderer engine = getEngine();
        String builderName = getName();
        if (!(engine instanceof TextRenderer)) {
            throw new UnsupportedOperationException();
        }

        String beginCoverTag = (String) context.getAttribute(builderName, "beginCoverTag");
        String endCoverTag = (String) context.getAttribute(builderName, "endCoverTag");

        Map<String, Object> tableInfo = context.getTableInfo(tableName);
        // Get output directory.
        File outputFile = new File(context.getOutputPath(builderName, tableName));
        File outputDir = outputFile.getParentFile();
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IOException("Directory \"" + outputDir + "\" create failure. ");
        }
        // Get variables.
        String outputCharset = context.getOutputCharset(builderName);
        String templateContent = templateContent(context);
        // Create template filled model.
        Dict model = Dict.of(context.getAttributes(builderName));
        model.set("buildTime", DateUtils.format(FULL_DATETIME_PATTERN));
        model.set("table", tableInfo);
        // Print log.
        String tmpString = "Generator \"{}\": rendering the java code corresponding to table \"{}\". ";
        log.info(tmpString, getName(), tableName);
        // Handle whether existing.
        if (outputFile.exists()) {
            if (context.skipExisted(builderName)) { return; }
            log.info("The file \"{}\" already exists, it will be try replace. ", outputFile.getName());
            // Generated content.
            Writer builderWriter = new StringBuilderWriter();
            ((TextRenderer) engine).render(templateContent, outputFile.getName(), model, builderWriter);
            String generation = builderWriter.toString();
            // Read file content.
            byte[] fileBytes = FileUtils.read(outputFile);
            String fileContent = new String(fileBytes, outputCharset);
            // Do replace.
            String outputStr = replaceContent(generation, fileContent, beginCoverTag, endCoverTag);
            // Write to file.
            if (outputStr == null) { return; }
            byte[] outputBytes = outputStr.getBytes(outputCharset);
            FileUtils.write(outputBytes, outputFile);
        }
        else {
            // Try create new file.
            if (!outputFile.createNewFile()) {
                throw new IOException("File \"" + outputFile + "\" create failure. ");
            }
            // Write to file.
            Writer writer = null;
            try {
                OutputStream output = new FileOutputStream(outputFile);
                writer = new OutputStreamWriter(output, outputCharset);
                ((TextRenderer) engine).render(templateContent, outputFile.getName(), model, writer);
            }
            finally {
                CloseUtils.closeQuietly(writer);
            }
        }
    }

    @Override
    public String build(FileContext context) {
        Assert.notNull(context, "Parameter \"context\" must not null. ");
        List<String> tableNames = context.getTableNames();
        for (String tableName : tableNames) {
            try {
                build(context, tableName);
            }
            catch (IOException e) {
                throw ExceptionUtils.wrap(e);
            }
        }
        return null;
    }

}
