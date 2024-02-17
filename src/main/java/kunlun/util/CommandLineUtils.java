/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import java.util.HashMap;
import java.util.Map;

import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.Symbols.DOUBLE_MINUS;
import static kunlun.common.constant.Symbols.EQUAL;

/**
 * Command line tools.
 * @author Kahle
 */
@Deprecated // TODO: can delete
public class CommandLineUtils {

    public static Map<String, String> parseParameters(String[] args) {
        Map<String, String> result = new HashMap<String, String>(args.length);
        if (ArrayUtils.isEmpty(args)) { return result; }
        for (String arg : args) {
            if (!arg.startsWith(DOUBLE_MINUS)) { continue; }
            arg = arg.substring(TWO);
            if (StringUtils.isBlank(arg)) { continue; }
            if (!arg.contains(EQUAL)) { continue; }
            int indexOf = arg.indexOf(EQUAL);
            String key = arg.substring(ZERO, indexOf);
            String val = arg.substring(indexOf + ONE, arg.length());
            result.put(key, val);
        }
        return result;
    }

}
