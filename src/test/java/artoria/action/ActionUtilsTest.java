package artoria.action;

import artoria.action.handler.AbstractClassicActionHandler;
import artoria.action.handler.AbstractOperateActionHandler;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ObjectUtils;
import org.junit.Test;

public class ActionUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ActionUtilsTest.class);
    private static String actionName1 = "Hello1";
    private static String actionName = "Hello";

    static {
        ActionUtils.registerHandler(actionName, new AbstractClassicActionHandler() {
            @Override
            public <T> T execute(Object input, Class<T> clazz) {
                // \u000dSystem.out.println("Hello, World! ");
                return ObjectUtils.cast("Hello, " + input + "！");
            }
        });
//        ActionUtils.registerHandler("class:" + String.class.getName(),
//                ActionUtils.getActionHandler(actionName));
        ActionUtils.registerHandler(actionName1, new AbstractOperateActionHandler() {
            @Override
            public Object operate(Object input, String name, Class<?> clazz) {
                return ObjectUtils.cast("[" + name + "] Hello, " + input + "！");
            }
        });
    }

    @Test
    public void test1() {
        System.out.println(ActionUtils.execute("Action Tools", actionName, String.class));
//        System.out.println(ActionUtils.execute("Action Tools", String.class));
        System.out.println(ActionUtils.execute(actionName, new Object[]{null, "Action Tools 1", String.class}));
    }

    @Test
    public void test2() {
        Object execute = ActionUtils.execute("Action Tools", actionName1, "fun1", String.class);
        System.out.println(execute);
        execute = ActionUtils.execute("Action Tools", actionName1, "fun2", String.class);
        System.out.println(execute);
    }

}
