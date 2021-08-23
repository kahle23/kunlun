package artoria.engine.template;

import artoria.util.ArrayUtils;
import artoria.util.StringUtils;

import static artoria.common.Constants.*;

/**
 * Log parameters renderer.
 */
public class LoggerTemplateEngine extends AbstractPlainTemplateEngine {
    private static final String PLACEHOLDER = LEFT_CURLY_BRACKET + RIGHT_CURLY_BRACKET;
    private static final Integer PLACEHOLDER_LENGTH = PLACEHOLDER.length();
    private static final char ESCAPE_SYMBOL = '\\';

    /*@Override
    public void render(Object data, Writer output, String logTag, String template) {
        Assert.notNull(output, "Parameter \"output\" must not null. ");
        Assert.isTrue(data == null || data instanceof Object[]
                , "Parameter \"data\" must instance of \"Object[]\". ");
        Object[] args = (Object[]) data;
        try {
            if (template == null || ArrayUtils.isEmpty(args)) {
                output.write(String.valueOf(template));
                return;
            }
            int index, start = ZERO, count = ZERO, argsLen = args.length, escapeIndex;
            while ((index = template.indexOf(PLACEHOLDER, start)) != MINUS_ONE) {
                boolean hasEscape = (escapeIndex = index - TWO) < ZERO
                        || ESCAPE_SYMBOL != template.charAt(escapeIndex);
                hasEscape = hasEscape && (escapeIndex = index - ONE) >= ZERO;
                if (hasEscape && ESCAPE_SYMBOL == template.charAt(escapeIndex)) {
                    output.write(template.substring(start, escapeIndex));
                    output.write(PLACEHOLDER);
                    start = index + PLACEHOLDER_LENGTH;
                    continue;
                }
                output.write(template.substring(start, index));
                output.write(String.valueOf(count < argsLen ? args[count++] : PLACEHOLDER));
                start = index + PLACEHOLDER_LENGTH;
            }
            if (start < template.length()) { output.write(template.substring(start)); }
        }
        catch (IOException e) {
            throw new RenderException(e);
        }
    }*/

    @Override
    public String render(String template, Object[] arguments) {
//        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
//        Assert.notNull(template, "Parameter \"template\" must not null. ");
        if (template == null) { return null; }
        if (StringUtils.isBlank(template)) { return template; }
        if (ArrayUtils.isEmpty(arguments)) { return template; }
        StringBuilder result = new StringBuilder();
        int index, start = 0, count = 0, argsLen = arguments.length, escapeIndex;
        while ((index = template.indexOf(PLACEHOLDER, start)) != MINUS_ONE) {
            boolean hasEscape = (escapeIndex = index - TWO) < ZERO
                    || ESCAPE_SYMBOL != template.charAt(escapeIndex);
            hasEscape = hasEscape && (escapeIndex = index - ONE) >= ZERO;
            if (hasEscape && ESCAPE_SYMBOL == template.charAt(escapeIndex)) {
                result.append(template, start, escapeIndex);
                result.append(PLACEHOLDER);
                start = index + PLACEHOLDER_LENGTH;
                continue;
            }
            result.append(template, start, index);
            result.append(String.valueOf(count < argsLen ? arguments[count++] : PLACEHOLDER));
            start = index + PLACEHOLDER_LENGTH;
        }
        if (start < template.length()) { result.append(template.substring(start)); }
        return result.toString();
    }

}
