package artoria.renderer.support;

import artoria.data.bean.BeanUtils;
import artoria.data.tuple.Pair;
import artoria.exception.ExceptionUtils;
import artoria.io.util.IOUtils;
import artoria.util.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;

import static artoria.common.Constants.*;
import static artoria.io.util.IOUtils.EOF;
import static artoria.util.ObjectUtils.cast;

/**
 * The simple text template renderer based on JDK.
 * @author Kahle
 */
public class SimpleTextRenderer extends AbstractTextRenderer {
    private static final String LEFT_PLACEHOLDER = "${";
    private static final String RIGHT_PLACEHOLDER = "}";
    private static final char ESCAPE_SYMBOL = '\\';

    protected Reader createClasspathReader(String path, String encoding) {
        Assert.notBlank(path, "Parameter \"path\" must not blank. ");
        InputStream inputStream = ClassLoaderUtils.getResourceAsStream(path, getClass());
        Assert.notNull(inputStream, "Can not find template by \"" + path + "\" in classpath. ");
        if (StringUtils.isBlank(encoding)) { encoding = DEFAULT_ENCODING_NAME; }
        Charset charset = Charset.forName(encoding);
        return new InputStreamReader(inputStream, charset);
    }

    protected String render(String template, Map<?, ?> data) throws ParseException {
        // Parameters check.
        if (StringUtils.isBlank(template)) { return null; }
        if (MapUtils.isEmpty(data)) { return template; }
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
    public void render(Object template, String name, Object data, Object output) {
        // Parameters check and conversion.
        Assert.isInstanceOf(Writer.class, output, "Parameter \"output\" must instance of Writer. ");
        if (template == null) { return; }
        Map<String, Object> dataMap = data != null
                ? BeanUtils.beanToMap(data) : Collections.<String, Object>emptyMap();
        Writer writer = (Writer) output;
        // Get template content.
        Reader templateReader = null;
        String templateStr;
        try {
            if (template instanceof String) {
                templateStr = (String) template;
            }
            else if (template instanceof Reader) {
                templateReader = (Reader) template;
                templateStr = IOUtils.toString(templateReader);
            }
            else if (template instanceof Pair) {
                Pair<String, String> pair = cast(template);
                String encoding = pair.getRight();
                String path = pair.getLeft();
                Reader reader = createClasspathReader(path, encoding);
                templateReader = reader;
                templateStr = IOUtils.toString(reader);
            }
            else {
                throw new IllegalArgumentException();
            }
            // Do render.
            String render = render(templateStr, dataMap);
            writer.write(render);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(templateReader);
            CloseUtils.closeQuietly(writer);
        }
    }

}
