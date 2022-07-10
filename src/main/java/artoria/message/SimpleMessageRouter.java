package artoria.message;

import artoria.lang.Router;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

import static artoria.common.Constants.*;

/**
 * The simple message router.
 * @author Kahle
 */
public class SimpleMessageRouter implements Router {

    @Override
    public Object route(Object[] arguments) {
        // Check of parameters.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= FOUR,
                "The length of parameter \"arguments\" must be at least 4. ");
        // Get arguments.
        // Parameter "arguments" usually is:
        // 0 handler name, 1 operation name, 2 input or message, 3 type
        String handlerName = (String) arguments[ZERO];
        String operation = (String) arguments[ONE];
        Object input = arguments[TWO];
        Type   type = (Type) arguments[THREE];
        // If the handler name is not empty, return it directly.
        if (StringUtils.isNotBlank(handlerName)) { return handlerName; }
        // The input parameter should not be null.
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        // The input parameter is the processing logic for the List.
        if (input instanceof List) {
            List<?> messages = (List<?>) input;
            Class<?> prevType = null;
            for (Object message : messages) {
                Assert.notNull(message, "Parameter \"messages\"'s elements all must not null. ");
                Class<?> nowType = message.getClass();
                if (prevType == null) { prevType = nowType; }
                Assert.isTrue(nowType.equals(prevType)
                        , "Parameter \"messages\"'s elements type all must be equal. ");
            }
            return prevType != null ? "class:" + prevType.getName() : null;
        }
        // Other scenarios concatenate class names directly.
        return "class:" + input.getClass().getName();
    }

}
