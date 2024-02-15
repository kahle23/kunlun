package artoria.renderer.support;

import artoria.util.ArrayUtils;
import artoria.util.StringUtils;

import static artoria.common.constant.Numbers.*;
import static artoria.common.constant.Symbols.LEFT_CURLY_BRACKET;
import static artoria.common.constant.Symbols.RIGHT_CURLY_BRACKET;

/**
 * The logger format text renderer.
 * @author Kahle
 */
public class LoggerTextRenderer extends AbstractFormatTextRenderer {
    private static final String PLACEHOLDER = LEFT_CURLY_BRACKET + RIGHT_CURLY_BRACKET;
    private static final Integer PLACEHOLDER_LENGTH = PLACEHOLDER.length();
    private static final char ESCAPE_SYMBOL = '\\';

    @Override
    public String render(String template, Object[] arguments) {
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
            result.append(count < argsLen ? arguments[count++] : PLACEHOLDER);
            start = index + PLACEHOLDER_LENGTH;
        }
        if (start < template.length()) { result.append(template.substring(start)); }
        return result.toString();
    }

}
