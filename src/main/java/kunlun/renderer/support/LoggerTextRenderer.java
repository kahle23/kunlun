/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer.support;

import kunlun.util.ArrayUtils;
import kunlun.util.StringUtils;

import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.Symbols.LEFT_CURLY_BRACKET;
import static kunlun.common.constant.Symbols.RIGHT_CURLY_BRACKET;

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
