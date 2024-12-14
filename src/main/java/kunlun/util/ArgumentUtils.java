/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.data.tuple.Pair;
import kunlun.data.tuple.PairImpl;
import kunlun.data.tuple.Triple;
import kunlun.data.tuple.TripleImpl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.Symbols.DOUBLE_MINUS;
import static kunlun.common.constant.Symbols.EQUAL;

/**
 * The arguments tool.
 * @author Kahle
 */
public class ArgumentUtils {

    /**
     * Parsing the arguments array.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null
     *      1 input object
     *      2 return value type (Type)
     * @param arguments The arguments
     * @return The result
     */
    @Deprecated
    public static Triple<Object, String, Type> parseToObjStrType(Object[] arguments) {
        // Verification arguments.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= THREE
                , "The length of parameter \"arguments\" must be at least 3. ");
        // Check return value type.
        Assert.notNull(arguments[TWO], "Parameter \"arguments[2]\" must not null. ");
        Assert.isInstanceOf(Type.class, arguments[TWO]
                , "Parameter \"arguments[2]\" must instance of Type. ");
        // Extract parameters.
        String strategy = arguments[ZERO] != null ? String.valueOf(arguments[ZERO]) : null;
        Type type = (Type) arguments[TWO];
        Object input = arguments[ONE];
        // Method call.
        return new TripleImpl<Object, String, Type>(input, strategy, type);
    }

    /**
     * Parsing the arguments array.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null
     *      1 input object
     * @param arguments The arguments
     * @return The result
     */
    public static Triple<String, Object, Object[]> parseToStrObjArr(Object[] arguments) {
        // Verification arguments.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= TWO
                , "The length of parameter \"arguments\" must be at least 2. ");
        // Extract parameters.
        String strategy = arguments[ZERO] != null ? String.valueOf(arguments[ZERO]) : null;
        Object input = arguments[ONE];
        int newLength = arguments.length - TWO;
        Object[] newArgs = new Object[newLength];
        if (newLength > ZERO) {
            System.arraycopy(arguments, TWO, newArgs, ZERO, newLength);
        }
        // Result.
        return new TripleImpl<String, Object, Object[]>(strategy, input, newArgs);
    }

    /**
     * Parsing the arguments array.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null
     *      1 input object
     *      2 return value type (Class)
     * @param arguments The arguments
     * @return The result
     */
    @Deprecated
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

    /**
     * Parsing the arguments array.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null
     *      1 new arguments array
     * @param arguments The arguments
     * @return The result
     */
    @Deprecated
    public static Pair<Object, Object[]> parseToObjArr(Object[] arguments) {
        // Check parameters.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= ONE
                , "The length of parameter \"arguments\" must be at least 1. ");
        // Extract parameters.
        int newLength = arguments.length - ONE;
        Object[] newArgs = new Object[newLength];
        if (newLength > ZERO) {
            System.arraycopy(arguments, ONE, newArgs, ZERO, newLength);
        }
        Object strategy = arguments[ZERO];
        // Method call.
        return new PairImpl<Object, Object[]>(strategy, newArgs);
    }

    /**
     * Parsing the arguments array.
     * The arguments mean (most of the scenes):
     *      0 input object
     *      1 return value type
     * @param arguments The arguments
     * @return The result
     */
    @Deprecated
    public static Pair<Object, Class<?>> parseToObjCls(Object[] arguments) {
        // Verification arguments.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= TWO
                , "The length of parameter \"arguments\" must be at least 2. ");
        // Check return value type.
        Assert.notNull(arguments[ZERO], "Parameter \"arguments[0]\" must not null. ");
        Assert.notNull(arguments[ONE], "Parameter \"arguments[1]\" must not null. ");
        Assert.isInstanceOf(Class.class, arguments[ONE]
                , "Parameter \"arguments[1]\" must instance of class. ");
        // Extract parameters.
        Class<?> clazz = (Class<?>) arguments[ONE];
        Object input = arguments[ZERO];
        // Method call.
        return new PairImpl<Object, Class<?>>(input, clazz);
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
