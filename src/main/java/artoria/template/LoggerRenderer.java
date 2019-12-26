package artoria.template;

import artoria.exception.ExceptionUtils;
import artoria.util.ArrayUtils;
import artoria.util.Assert;

import java.io.Writer;

import static artoria.common.Constants.*;

/**
 * Log parameters renderer.
 * @author Kahle
 */
public class LoggerRenderer implements Renderer {
    private static final String PLACEHOLDER = LEFT_CURLY_BRACKET + RIGHT_CURLY_BRACKET;
    private static final Integer PLACEHOLDER_LENGTH = PLACEHOLDER.length();
    private static final char ESCAPE_SYMBOL = '\\';

    @Override
    public void render(Object data, Object output, String name, Object input, String charsetName) throws RenderException {
        try {
            Assert.state(input == null || input instanceof String
                    , "Parameter \"input\" must instance of \"String\". ");
            Assert.state(data == null || data instanceof Object[]
                    , "Parameter \"data\" must instance of \"Object[]\". ");
            Assert.state(output instanceof Writer
                    , "Parameter \"output\" must instance of \"Writer\". ");
            String format = (String) input;
            Object[] args = (Object[]) data;
            Writer writer = (Writer) output;
            if (format == null || ArrayUtils.isEmpty(args)) {
                writer.write(String.valueOf(format));
                return;
            }
            int index, start = ZERO, count = ZERO, argsLen = args.length, escapeIndex;
            while ((index = format.indexOf(PLACEHOLDER, start)) != MINUS_ONE) {
                boolean hasEscape = (escapeIndex = index - TWO) < ZERO
                        || ESCAPE_SYMBOL != format.charAt(escapeIndex);
                hasEscape = hasEscape && (escapeIndex = index - ONE) >= ZERO;
                if (hasEscape && ESCAPE_SYMBOL == format.charAt(escapeIndex)) {
                    writer.write(format.substring(start, escapeIndex));
                    writer.write(PLACEHOLDER);
                    start = index + PLACEHOLDER_LENGTH;
                    continue;
                }
                writer.write(format.substring(start, index));
                writer.write(String.valueOf(count < argsLen ? args[count++] : PLACEHOLDER));
                start = index + PLACEHOLDER_LENGTH;
            }
            if (start < format.length()) { writer.write(format.substring(start)); }
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
