package artoria.action;

import artoria.lang.Router;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.lang.reflect.Type;

import static artoria.common.Constants.*;

/**
 * The simple action router.
 * @author Kahle
 */
public class SimpleActionRouter implements Router {

    @Override
    public Object route(Object[] arguments) {
        // Check of parameters.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= FOUR,
                "The length of parameter \"arguments\" must be at least 4. ");
        // Get arguments.
        // Parameter "arguments" usually is: 0 action name, 1 strategy or operation, 2 input, 3 type
        String actionName = (String) arguments[ZERO];
        Object strategy = arguments[ONE];
        Object input = arguments[TWO];
        Type   type = (Type) arguments[THREE];
        // If the action name is not empty, return it directly.
        if (StringUtils.isNotBlank(actionName)) { return actionName; }
        // The input parameter should not be null.
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        // Other scenarios concatenate class names directly.
        return "class:" + input.getClass().getName();
    }

}
