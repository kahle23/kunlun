package artoria.util;

import artoria.data.tuple.Triple;
import artoria.data.tuple.TripleImpl;

import java.util.HashMap;
import java.util.Map;

import static artoria.common.constant.Numbers.*;
import static artoria.common.constant.Symbols.DOUBLE_MINUS;
import static artoria.common.constant.Symbols.EQUAL;

/**
 * The arguments tool.
 * @author Kahle
 */
public class ArgumentUtils {

    /**
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param arguments  The arguments
     * @return The result
     */
    public static Triple<Object, String, Class<?>> parseToObjStrCls(Object[] arguments) {
        // Verification arguments.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= THREE
                , "The length of parameter \"arguments\" must be at least 3. ");
        // Check return value type.
        Assert.notNull(arguments[TWO], "Parameter \"arguments[2]\" must not null. ");
        Assert.isInstanceOf(Class.class, arguments[TWO]
                , "Parameter \"arguments[2]\" must instance of Class. ");
        // Extract parameters.
        String strategy = arguments[ZERO] != null ? String.valueOf(arguments[ZERO]) : null;
        Class<?> clazz = (Class<?>) arguments[TWO];
        Object input = arguments[ONE];
        // Method call.
        return new TripleImpl<Object, String, Class<?>>(input, strategy, clazz);
    }

    public static Map<String, String> parseToMap(String[] arguments) {
        Map<String, String> result = new HashMap<String, String>(arguments.length);
        if (ArrayUtils.isEmpty(arguments)) { return result; }
        for (String arg : arguments) {
            if (!arg.startsWith(DOUBLE_MINUS)) { continue; }
            arg = arg.substring(TWO);
            if (StringUtils.isBlank(arg)) { continue; }
            if (!arg.contains(EQUAL)) { continue; }
            int indexOf = arg.indexOf(EQUAL);
            String key = arg.substring(ZERO, indexOf);
            String val = arg.substring(indexOf + ONE);
            result.put(key, val);
        }
        return result;
    }

}
