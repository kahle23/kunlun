package artoria.util;

import java.util.HashMap;
import java.util.Map;

import static artoria.common.Constants.*;

/**
 * Command line tools.
 * @author Kahle
 */
public class CommandLineUtils {

    public static Map<String, String> parseParameters(String[] args) {
        Map<String, String> result = new HashMap<String, String>(args.length);
        if (ArrayUtils.isEmpty(args)) { return result; }
        String separator = "--"; int sepLen = separator.length();
        for (String arg : args) {
            if (!arg.startsWith(separator)) { continue; }
            arg = arg.substring(sepLen);
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
