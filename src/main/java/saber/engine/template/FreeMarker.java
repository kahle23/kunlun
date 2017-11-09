package saber.engine.template;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * @author Kahle
 */
public class FreeMarker implements TemplateEngine<freemarker.template.Template> {

    public static FreeMarker me = FreeMarker.on(new Configuration());

    public static FreeMarker on() {
        return new FreeMarker(new Configuration());
    }

    public static FreeMarker on(Configuration configuration) {
        return new FreeMarker(configuration);
    }

    private Configuration configuration;
    private List<TemplateLoader> loaders = new ArrayList<>();
    private StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

    private FreeMarker(Configuration configuration) {
        this.configuration = configuration;
        addTemplateLoaders(this.configuration.getTemplateLoader(), stringTemplateLoader);
    }

    public FreeMarker setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public FreeMarker setTemplateLoader(TemplateLoader loader) {
        configuration.setTemplateLoader(loader);
        return this;
    }

    public FreeMarker setTemplateLoader(TemplateLoader... loaders) {
        MultiTemplateLoader loader = new MultiTemplateLoader(loaders);
        configuration.setTemplateLoader(loader);
        return this;
    }

    public TemplateLoader getTemplateLoader() {
        return configuration.getTemplateLoader();
    }

    public FreeMarker addTemplateLoaders(TemplateLoader... loaders) {
        this.loaders.addAll(Arrays.asList(loaders));
        TemplateLoader[] loaderArr = new TemplateLoader[this.loaders.size()];
        this.loaders.toArray(loaderArr);
        configuration.setTemplateLoader(new MultiTemplateLoader(loaderArr));
        return this;
    }

    public FreeMarker addTemplateLoaders(List<TemplateLoader> loaders) {
        this.loaders.addAll(loaders);
        TemplateLoader[] loaderArr = new TemplateLoader[this.loaders.size()];
        this.loaders.toArray(loaderArr);
        configuration.setTemplateLoader(new MultiTemplateLoader(loaderArr));
        return this;
    }

    @Override
    public FreeMarker clearTemplateCache() throws Exception {
        configuration.clearTemplateCache();
        return this;
    }

    @Override
    public Template getTemplate(String templateName) throws Exception {
        return new FreeMarkerTemplate().setTemplate(configuration.getTemplate(templateName));
    }

    @Override
    public Template getTemplate(freemarker.template.Template template) throws Exception {
        return new FreeMarkerTemplate().setTemplate(template);
    }

    @Override
    public Template getTemplate(String templateName, String encoding) throws Exception {
        return new FreeMarkerTemplate().setTemplate(configuration.getTemplate(templateName, encoding));
    }

    @Override
    public Template getTemplateByString(String templateSource) throws Exception {
        return getTemplateByString(null, templateSource, false);
    }

    @Override
    public Template getTemplateByString(String templateName, String templateSource) throws Exception {
        return getTemplateByString(templateName, templateSource, false);
    }

    @Override
    public Template getTemplateByString(String templateName, String templateSource, boolean cache) throws Exception {
        if (cache) {
            stringTemplateLoader.putTemplate(templateName, templateSource);
            return getTemplate(templateName);
        }
        else {
            return new FreeMarkerTemplate()
                    .setTemplate(new freemarker.template.Template(templateName, templateSource, configuration));
        }
    }

    @Override
    public Template getTemplateByReader(Reader reader) throws Exception {
        return getTemplateByReader(null, reader, false);
    }

    @Override
    public Template getTemplateByReader(String templateName, Reader reader) throws Exception {
        return getTemplateByReader(templateName, reader, false);
    }

    @Override
    public Template getTemplateByReader(String templateName, Reader reader, boolean cache) throws Exception {
        if (cache) {
            stringTemplateLoader.putTemplate(templateName, IOUtils.toString(reader));
            return getTemplate(templateName);
        }
        else {
            return new FreeMarkerTemplate()
                    .setTemplate(new freemarker.template.Template(templateName, reader, configuration));
        }
    }

    private static class FreeMarkerTemplate implements Template<freemarker.template.Template> {
        private freemarker.template.Template template;

        @Override
        public freemarker.template.Template getTemplate() {
            return template;
        }

        @Override
        public Template setTemplate(freemarker.template.Template template) {
            this.template = template;
            return this;
        }

        @Override
        public Template render(Map data, Writer writer) throws Exception {
            template.process(data, writer);
            return this;
        }

        @Override
        public Template render(Writer writer) throws Exception {
            template.process(new HashMap(0), writer);
            return this;
        }

        @Override
        public String renderToString(Map data) throws Exception {
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            return writer.toString();
        }

        @Override
        public StringBuilder renderToStringBuilder(Map data) throws Exception {
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            return new StringBuilder(writer.getBuffer());
        }

    }

}
