package saber.engine.template;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FreeMarker {

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

    public FreeMarker clearTemplateCache() {
        configuration.clearTemplateCache();
        return this;
    }

    public Template getTemplate(String name) throws IOException {
        return configuration.getTemplate(name);
    }

    public Template getTemplate(String name, Locale locale) throws IOException {
        return configuration.getTemplate(name, locale);
    }

    public Template getTemplate(String name, String encoding) throws IOException {
        return configuration.getTemplate(name, encoding);
    }

    public Template getTemplate(String name, Locale locale, String encoding) throws IOException {
        return configuration.getTemplate(name, locale, encoding);
    }

    public Template getTemplate(String name, Locale locale, String encoding, boolean parseAsFTL) throws IOException {
        return configuration.getTemplate(name, locale, encoding, parseAsFTL);
    }

    public FreeMarker addTemplate(String name, String templateSource) throws IOException {
        stringTemplateLoader.putTemplate(name, templateSource);
        return this;
    }

    public FreeMarker addTemplate(String name, String templateSource, long lastModified) throws IOException {
        stringTemplateLoader.putTemplate(name, templateSource, lastModified);
        return this;
    }

    public FreeMarker handle(String templateSource, Object dataModel, Writer out) throws IOException, TemplateException {
        Template template = new Template(null, templateSource, configuration);
        template.process(dataModel, out);
        return this;
    }

    public FreeMarker handle(Reader reader, Object dataModel, Writer out) throws IOException, TemplateException {
        Template template = new Template(null, reader, configuration);
        template.process(dataModel, out);
        return this;
    }

    public String handleString(String templateSource, Object dataModel) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        handle(templateSource, dataModel, writer);
        return writer.toString();
    }

    public String handleString(Reader reader, Object dataModel) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        handle(reader, dataModel, writer);
        return writer.toString();
    }

    public FreeMarker process(Template template, Object dataModel, Writer out) throws IOException, TemplateException {
        template.process(dataModel, out);
        return this;
    }

    public FreeMarker process(Template template, Object dataModel, Writer out, ObjectWrapper wrapper) throws IOException, TemplateException {
        template.process(dataModel, out, wrapper);
        return this;
    }

    public FreeMarker process(Template template, Object dataModel, Writer out, ObjectWrapper wrapper, TemplateNodeModel rootNode) throws IOException, TemplateException {
        template.process(dataModel, out, wrapper, rootNode);
        return this;
    }

    public FreeMarker process(String templateName, Object dataModel, Writer out) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templateName);
        process(template, dataModel, out);
        return this;
    }

    public String processString(Template template, Object dataModel) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        process(template, dataModel, writer);
        return writer.toString();
    }

    public String processString(Template template, Object dataModel, ObjectWrapper wrapper) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        process(template, dataModel, writer, wrapper);
        return writer.toString();
    }

    public String processString(Template template, Object dataModel, ObjectWrapper wrapper, TemplateNodeModel rootNode) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        process(template, dataModel, writer, wrapper, rootNode);
        return writer.toString();
    }

    public String processString(String TemplateName, Object dataModel) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        process(TemplateName, dataModel, writer);
        return writer.toString();
    }

}
