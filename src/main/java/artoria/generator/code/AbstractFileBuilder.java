package artoria.generator.code;

import artoria.core.Renderer;
import artoria.exception.ExceptionUtils;
import artoria.io.util.IOUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;
import artoria.util.CloseUtils;
import artoria.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static artoria.common.constant.Numbers.ZERO;
import static artoria.io.util.IOUtils.EOF;

public abstract class AbstractFileBuilder implements FileBuilder {
    private static final Logger log = LoggerFactory.getLogger(AbstractFileBuilder.class);
    private static final String CLASSPATH = "classpath:";
    private final Renderer engine;
    private final String name;
    private String templateContent;
    private String templatePath;

    protected AbstractFileBuilder(String name, Renderer engine) {
        this.engine = engine;
        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Renderer getEngine() {

        return engine;
    }

    protected String templateContent(FileContext context) {
        String templatePath = context.getTemplatePath(getName());
        if (StringUtils.equals(templatePath, this.templatePath)
                &&StringUtils.isNotBlank(templateContent)) {
            return templateContent;
        }
        InputStream in = null;
        try {
            in = templatePath.startsWith(CLASSPATH) ?
                    ClassLoaderUtils.getResourceAsStream(
                            templatePath.substring(CLASSPATH.length()), this.getClass()
                    ) :
                    new FileInputStream(templatePath);
            String templateCharset = context.getTemplateCharset(getName());
            templateContent = IOUtils.toString(in, templateCharset);
            this.templatePath = templatePath;
            return templateContent;
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(in);
        }
    }

    protected String replaceContent(String newContent, String oldContent, String beginCoverTag, String endCoverTag) {
        Assert.notBlank(newContent, "Parameter \"newContent\" must not blank. ");
        if (StringUtils.isBlank(beginCoverTag)) { return newContent; }
        if (StringUtils.isBlank(endCoverTag)) { return newContent; }
        if (StringUtils.isBlank(oldContent)) { return newContent; }
        // Variable definition.
        int oldIndex = ZERO, newIndex = ZERO, count = ZERO, tmpFromIndex;
        StringBuilder result = new StringBuilder();
        do {
            // Look for the begin cover tag in the old content.
            int oldBegin = oldContent.indexOf(beginCoverTag, oldIndex);
            // If don't find it once.
            if (oldBegin == EOF && count == ZERO) {
                log.info("Can not find begin cover tag in old content. ");
                return null;
            }
            // No begin cover tag found, indicating that processing is complete.
            if (oldBegin == EOF) {
                int oldEnd = oldContent.indexOf(endCoverTag, oldIndex);
                if (oldEnd != EOF) {
                    log.info("The end cover tag should not exist after index {} in old content. "
                            , oldIndex);
                    return null;
                }
                int newBegin = newContent.indexOf(beginCoverTag, newIndex);
                if (newBegin != EOF) {
                    log.info("The begin cover tag should not exist after index {} in new content. "
                            , newIndex);
                    return null;
                }
                int newEnd = newContent.indexOf(endCoverTag, newIndex);
                if (newEnd != EOF) {
                    log.info("The end cover tag should not exist after index {} in new content. "
                            , newIndex);
                    return null;
                }
                result.append(oldContent, oldIndex, oldContent.length());
                return result.toString();
            }
            // Look for the end cover tag in the old content.
            tmpFromIndex = oldBegin + beginCoverTag.length();
            int oldEnd = oldContent.indexOf(endCoverTag, tmpFromIndex);
            if (oldEnd == EOF) {
                log.info("Can not find end cover tag in old content. ");
                return null;
            }
            // Look for the begin cover tag in the new content.
            int newBegin = newContent.indexOf(beginCoverTag, newIndex);
            if (newBegin == EOF) {
                log.info("Can not find begin cover tag in new content. ");
                return null;
            }
            // Look for the end cover tag in the new content.
            tmpFromIndex = newBegin + beginCoverTag.length();
            int newEnd = newContent.indexOf(endCoverTag, tmpFromIndex);
            if (newEnd == EOF) {
                log.info("Can not find end cover tag in new content. ");
                return null;
            }
            // The assembly results.
            result.append(oldContent, oldIndex, oldBegin);
            result.append(newContent, newBegin, newEnd);
            result.append(endCoverTag);
            oldIndex = oldEnd + endCoverTag.length();
            newIndex = newEnd + endCoverTag.length();
            count++;
        } while (true);
    }

}
