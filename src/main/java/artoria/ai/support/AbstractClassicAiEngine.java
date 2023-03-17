package artoria.ai.support;

import artoria.ai.AbstractAiEngine;
import artoria.util.Assert;

import static artoria.common.Constants.*;

/**
 * The abstract classic ai engine.
 * @author Kahle
 */
public abstract class AbstractClassicAiEngine extends AbstractAiEngine {

    @Override
    public Object execute(Object[] arguments) {
        // Check parameters.
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
        return execute(input, strategy, clazz);
    }

    /**
     * Execute a specific logic.
     * @param input The input parameters to be executed
     * @param strategy The input parameters to be executed
     * @param clazz The class of the return value
     * @return The return value
     */
    public abstract Object execute(Object input, String strategy, Class<?> clazz);

}
