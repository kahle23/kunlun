/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.code.support.java;

import kunlun.core.Renderer;
import kunlun.data.Dict;
import kunlun.exception.ExceptionUtils;
import kunlun.generator.code.AbstractFileBuilder;
import kunlun.generator.code.FileContext;
import kunlun.io.util.FileUtils;
import kunlun.io.util.StringBuilderWriter;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.renderer.TextRenderer;
import kunlun.time.DateUtils;
import kunlun.util.Assert;
import kunlun.util.CloseUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

import static kunlun.common.constant.TimePatterns.UTC_MS;

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
        model.set("buildTime", DateUtils.format(UTC_MS));
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
