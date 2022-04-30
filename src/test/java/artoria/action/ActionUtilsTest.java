package artoria.action;

import artoria.action.handler.AbstractClassicActionHandler;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ObjectUtils;
import org.junit.Test;

public class ActionUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ActionUtilsTest.class);
    private static String actionName = "Hello";

    static {
        ActionUtils.registerHandler(actionName, new AbstractClassicActionHandler() {
            @Override
            public <T> T execute(Object input, Class<T> clazz) {
                // \u000dSystem.out.println("Hello, World! ");
                return ObjectUtils.cast("Hello, " + input + "！");
            }
        });
    }

    @Test
    public void test1() {
        System.out.println(ActionUtils.execute("Action Tools", actionName, String.class));
        System.out.println(ActionUtils.execute(actionName, new Object[]{"Action Tools 1", String.class}));
    }

}
