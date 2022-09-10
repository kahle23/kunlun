package artoria.renderer.support.text;

import artoria.beans.BeanUtils;
import artoria.exception.ExceptionUtils;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.io.Writer;
import java.text.ParseException;
import java.util.Map;

import static artoria.common.Constants.*;
import static artoria.io.IOUtils.EOF;

/**
 * Template renderer simple implement by jdk.
 */
public class SimpleTextRenderer extends AbstractNormalTextRenderer {
    private static final String LEFT_PLACEHOLDER = DOLLAR_SIGN + LEFT_CURLY_BRACKET;
    private static final String RIGHT_PLACEHOLDER = RIGHT_CURLY_BRACKET;
    private static final char ESCAPE_SYMBOL = '\\';

    protected String render(String template, Map data) throws ParseException {
        StringBuilder result = new StringBuilder();
        int finish = template.length(), begin = ZERO, end = ZERO, escapeIndex;
        while (end != finish) {
            end = template.indexOf(LEFT_PLACEHOLDER, begin);
            if (end != EOF) {
                boolean hasEscape = (escapeIndex = end - TWO) < ZERO
                        || ESCAPE_SYMBOL != template.charAt(escapeIndex);
                hasEscape = hasEscape && (escapeIndex = end - ONE) >= ZERO;
                if (hasEscape && ESCAPE_SYMBOL == template.charAt(escapeIndex)) {
                    result.append(template, begin, escapeIndex);
                    result.append(LEFT_PLACEHOLDER);
                    begin = end + LEFT_PLACEHOLDER.length();
                    continue;
                }
            }
            end = end == EOF ? finish : end;
            result.append(template, begin, end);
            if (end == finish) { continue; }
            end = template.indexOf(RIGHT_PLACEHOLDER, (begin = end + TWO));
            if (end == EOF) {
                throw new ParseException("After \"${\" must be \"}\" in index \"" + begin + "\". ", begin);
            }
            Object obj = data.get(template.substring(begin, end));
            result.append(obj != null ? obj.toString() : EMPTY_STRING);
            begin = ++end;
        }
        return result.toString();
    }

    @Override
    public void render(Object data, Writer writer, String tag, String template) {
        Assert.notNull(writer, "Parameter \"output\" must not null. ");
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        if (StringUtils.isBlank(template)) { return; }
        Map dataMap = data instanceof Map ? (Map) data : BeanUtils.createBeanMap(data);
        try {
            /*for (int finish = template.length(), begin = ZERO, end = ZERO, escapeIndex; end != finish; ) {
                end = template.indexOf(LEFT_PLACEHOLDER, begin);
                if (end != EOF) {
                    boolean hasEscape = (escapeIndex = end - TWO) < ZERO || ESCAPE_SYMBOL != template.charAt(escapeIndex);
                    hasEscape = hasEscape && (escapeIndex = end - ONE) >= ZERO;
                    if (hasEscape && ESCAPE_SYMBOL == template.charAt(escapeIndex)) {
                        output.write(template.substring(begin, escapeIndex));
                        output.write(LEFT_PLACEHOLDER);
                        begin = end + LEFT_PLACEHOLDER.length();
                        continue;
                    }
                }
                end = end == EOF ? finish : end;
                output.write(template.substring(begin, end));
                if (end == finish) { continue; }
                end = template.indexOf(RIGHT_PLACEHOLDER, (begin = end + TWO));
                if (end == EOF) {
                    throw new ParseException("After \"${\" must be \"}\" in index \"" + begin + "\". ", begin);
                }
                Object obj = dataMap.get(template.substring(begin, end));
                output.write(obj != null ? obj.toString() : EMPTY_STRING);
                begin = ++end;
            }*/
            String render = render(template, dataMap);
            writer.write(render);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
