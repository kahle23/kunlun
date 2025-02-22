/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer.support;

import kunlun.data.bean.BeanUtils;
import kunlun.exception.ExceptionUtils;
import kunlun.io.util.IoUtil;
import kunlun.util.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;

import static kunlun.common.constant.Charsets.STR_UTF_8;
import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.Symbols.EMPTY_STRING;
import static kunlun.io.util.IoUtil.EOF;

/**
 * The simple text template renderer based on JDK.
 * @author Kahle
 */
public class SimpleTextRenderer extends AbstractTextRenderer {
    private static final String LEFT_PLACEHOLDER = "${";
    private static final String RIGHT_PLACEHOLDER = "}";
    private static final char ESCAPE_SYMBOL = '\\';

    protected void loadContent(Tpl tpl) {
        if (tpl == null || !ObjUtil.isEmpty(tpl.getContent())) { return; }
        if (StrUtil.isBlank(tpl.getCharset())) { tpl.setCharset(STR_UTF_8); }
        Charset charset = Charset.forName(tpl.getCharset());
        InputStream in = ClassLoaderUtils.getResourceAsStream(tpl.getName(), getClass());
        tpl.setContent(new InputStreamReader(Assert.notNull(in), charset));
    }

    protected String render(String template, Map<?, ?> data) throws ParseException {
        // Parameters check.
        if (StrUtil.isBlank(template)) { return null; }
        if (MapUtil.isEmpty(data)) { return template; }
        // Variable declarations.
        StringBuilder result = new StringBuilder();
        int finish = template.length(), begin = ZERO, end = ZERO, escapeIndex;
        // Loop to render the template.
        while (end != finish) {
            end = template.indexOf(LEFT_PLACEHOLDER, begin);
            // Escape character handling.
            if (end != EOF) {
                // Isn't two '\\'.
                boolean hasEscape = (escapeIndex = end - TWO) < ZERO
                        || ESCAPE_SYMBOL != template.charAt(escapeIndex);
                // Is one '\'.
                hasEscape = hasEscape && (escapeIndex = end - ONE) >= ZERO;
                if (hasEscape && ESCAPE_SYMBOL == template.charAt(escapeIndex)) {
                    // Append as a normal string.
                    result.append(template, begin, escapeIndex);
                    result.append(LEFT_PLACEHOLDER);
                    begin = end + LEFT_PLACEHOLDER.length();
                    continue;
                }
            }
            // Determine whether it is over.
            end = end == EOF ? finish : end;
            result.append(template, begin, end);
            if (end == finish) { continue; }
            // Computes the end index of the placeholder.
            end = template.indexOf(RIGHT_PLACEHOLDER, (begin = end + TWO));
            if (end == EOF) {
                throw new ParseException("After \"${\" must be \"}\" in index \"" + begin + "\". ", begin);
            }
            Object obj = data.get(template.substring(begin, end));
            result.append(obj != null ? obj.toString() : EMPTY_STRING);
            begin = ++end;
        }
        // Result.
        return result.toString();
    }

    @Override
    public void render(Object template, Object data, Object output) {
        // Parameters check and conversion.
        Writer writer = (Writer) Assert.isInstanceOf(Writer.class, output);
        if (template == null) { return; }
        Map<String, Object> dataMap = data != null
                ? BeanUtils.beanToMap(data) : Collections.<String, Object>emptyMap();
        // Get template content and render.
        Reader reader = null;
        try {
            if (template instanceof String) {
                writer.write(render((String) template, dataMap));
            } else if (template instanceof Reader) {
                String str = IoUtil.read(reader = (Reader) template);
                writer.write(render(str, dataMap));
            } else if (template instanceof Tpl) {
                Tpl tpl = (Tpl) template;
                if (ObjUtil.isEmpty(tpl.getContent())
                        && getTemplateLoader() != null) {
                    getTemplateLoader().accept(tpl);
                }
                if (ObjUtil.isEmpty(tpl.getContent())) {
                    loadContent(tpl);
                }
                render(tpl.getContent(), data, output);
            } else { throw new IllegalArgumentException("Unsupported template type! "); }
        } catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        } finally {
            IoUtil.closeQuietly(reader, writer);
        }
    }

}
