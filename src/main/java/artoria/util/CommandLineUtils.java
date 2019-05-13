package artoria.util;

import java.util.HashMap;
import java.util.Map;

import static artoria.common.Constants.EQUAL;

/**
 * Command line tools.
 * @author Kahle
 */
public class CommandLineUtils {
    private static final String MINUS_MINUS = "--";
    private static final int MINUS_MINUS_LENGTH = MINUS_MINUS.length();

    public static Map<String, String> parse(String[] args) {
        Map<String, String> result = new HashMap<String, String>(args.length);
        if (ArrayUtils.isEmpty(args)) { return result; }
        for (String arg : args) {
            if (!arg.startsWith(MINUS_MINUS)) { continue; }
            arg = arg.substring(MINUS_MINUS_LENGTH);
            if (StringUtils.isBlank(arg)) { continue; }
            if (!arg.contains(EQUAL)) { continue; }
            int indexOf = arg.indexOf(EQUAL);
            String key = arg.substring(0, indexOf);
            String val = arg.substring(indexOf + 1, arg.length());
            result.put(key, val);
        }
        return result;
    }

}
