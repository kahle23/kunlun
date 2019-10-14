package artoria.template;

import artoria.beans.BeanUtils;
import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;
import artoria.util.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.util.Map;

import static artoria.common.Constants.*;
import static artoria.io.IOUtils.EOF;

/**
 * Template renderer simple implement by jdk.
 * @author Kahle
 */
public class SimpleRenderer implements Renderer {
    private static final String LEFT_PLACEHOLDER = DOLLAR_SIGN + LEFT_CURLY_BRACKET;
    private static final String RIGHT_PLACEHOLDER = RIGHT_CURLY_BRACKET;
    private static final char ESCAPE_SYMBOL = '\\';

    protected void render(Object data, Writer writer, String template) throws IOException, ParseException {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        if (StringUtils.isBlank(template)) { return; }
        Map dataMap = data instanceof Map ? (Map) data : BeanUtils.beanToMap(data);
        for (int finish = template.length(), begin = ZERO, end = ZERO, escapeIndex; end != finish; ) {
            end = template.indexOf(LEFT_PLACEHOLDER, begin);
            if (end != EOF) {
                boolean hasEscape = (escapeIndex = end - TWO) < ZERO || ESCAPE_SYMBOL != template.charAt(escapeIndex);
                hasEscape = hasEscape && (escapeIndex = end - ONE) >= ZERO;
                if (hasEscape && ESCAPE_SYMBOL == template.charAt(escapeIndex)) {
                    writer.write(template.substring(begin, escapeIndex));
                    writer.write(LEFT_PLACEHOLDER);
                    begin = end + LEFT_PLACEHOLDER.length();
                    continue;
                }
            }
            end = end == EOF ? finish : end;
            writer.write(template.substring(begin, end));
            if (end == finish) { continue; }
            end = template.indexOf(RIGHT_PLACEHOLDER, (begin = end + TWO));
            if (end == EOF) {
                throw new ParseException("After \"${\" must be \"}\" in index \"" + begin + "\". ", begin);
            }
            Object obj = dataMap.get(template.substring(begin, end));
            writer.write(obj != null ? obj.toString() : EMPTY_STRING);
            begin = ++end;
        }
    }

    @Override
    public void render(Object data, Object output, String name, Object input, String charsetName) throws RenderException {
        try {
            Assert.state(output instanceof Writer, "Parameter \"output\" must instance of \"Writer\". ");
            Assert.notBlank(name, "Parameter \"name\" must not blank. ");
            String template;
            if (input instanceof Reader || input instanceof String) {
                template = input instanceof Reader ? IOUtils.toString((Reader) input) : (String) input;
            }
            else {
                InputStream in = ClassLoaderUtils.getResourceAsStream(name, getClass());
                if (in == null) {
                    throw new IOException("Can not find template by \"" + name + "\" in classpath. ");
                }
                charsetName = StringUtils.isNotBlank(charsetName) ? charsetName : DEFAULT_CHARSET_NAME;
                Reader reader = new InputStreamReader(in, charsetName);
                template = IOUtils.toString(reader);
            }
            render(data, (Writer) output, template);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e, RenderException.class);
        }
    }

}
