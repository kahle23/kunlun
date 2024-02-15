package artoria.ai.support;

import artoria.ai.AbstractAiHandler;
import artoria.util.Assert;

import static artoria.common.constant.Numbers.*;

/**
 * The abstract classic AI handler.
 * @author Kahle
 */
public abstract class AbstractClassicAiHandler extends AbstractAiHandler {

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
     * The artificial intelligence performs reasoning operations.
     * @param input The input parameters for inference calculations
     * @param operation The strategy or operation or null for AI handler execution
     * @param clazz The class of the return value
     * @return The result of the inference calculation
     */
    public abstract Object execute(Object input, String operation, Class<?> clazz);

}
