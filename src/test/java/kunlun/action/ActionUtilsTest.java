/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.action.support.AbstractClassicActionHandler;
import kunlun.action.support.AbstractOperateActionHandler;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.ObjectUtils;
import org.junit.Test;

/**
 * The action tools Test.
 * @author Kahle
 */
public class ActionUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ActionUtilsTest.class);
    private static final String actionName1 = "Hello1";
    private static final String actionName = "Hello";

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
