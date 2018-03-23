package com.github.kahlkn.artoria.template;

import com.github.kahlkn.artoria.io.StringBuilderWriter;
import com.github.kahlkn.artoria.logging.Logger;
import com.github.kahlkn.artoria.logging.LoggerFactory;
import com.github.kahlkn.artoria.util.Assert;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import static com.github.kahlkn.artoria.util.Const.DEFAULT_CHARSET_NAME;

/**
 * Template engine object.
 * @author Kahle
 */
public class TemplateEngine {
    private static Logger log = LoggerFactory.getLogger(TemplateEngine.class);
    private EngineAdapter adapter;

    public TemplateEngine(EngineAdapter adapter) {
        this.setAdapter(adapter);
    }

    public EngineAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(EngineAdapter adapter) {
        Assert.notNull(adapter, "Parameter \"adapter\" must not null. ");
        this.adapter = adapter;
    }

    public void render(String name, Object data, Writer writer) throws Exception {
        this.render(name, DEFAULT_CHARSET_NAME, data, writer);
    }

    public void render(String name, String encoding, Object data, Writer writer) throws Exception {
        adapter.render(name, encoding, data, writer);
    }

    public void render(Object data, Writer writer, String logTag, String template) throws Exception {
        Assert.notBlank(template, "Parameter \"template\" must not blank. ");
        StringReader reader = new StringReader(template);
        this.render(data, writer, logTag, reader);
    }

    public void render(Object data, Writer writer, String logTag, Reader reader) throws Exception {
        adapter.render(data, writer, logTag, reader);
    }

    public String renderToString(String name, Object data) throws Exception {
        return this.renderToString(name, DEFAULT_CHARSET_NAME, data);
    }

    public String renderToString(String name, String encoding, Object data) throws Exception {
        StringBuilderWriter writer = new StringBuilderWriter();
        adapter.render(name, encoding, data, writer);
        return writer.toString();
    }

    public String renderToString(Object data, String logTag, String template) throws Exception {
        Assert.notBlank(template, "Parameter \"template\" must not blank. ");
        StringReader reader = new StringReader(template);
        return this.renderToString(data, logTag, reader);
    }

    public String renderToString(Object data, String logTag, Reader reader) throws Exception {
        StringBuilderWriter writer = new StringBuilderWriter();
        adapter.render(data, writer, logTag, reader);
        return writer.toString();
    }

}
