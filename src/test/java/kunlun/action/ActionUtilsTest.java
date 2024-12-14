/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

/**
 * The action tools Test.
 * @author Kahle
 */
public class ActionUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ActionUtilsTest.class);
    private static final String actionName = "Hello";

    static {
        ActionUtils.registerAction(actionName, new AbstractAction() {
            @Override
            public Object execute(String strategy, Object input, Object[] arguments) {
                // \u000dSystem.out.println("Hello, World! ");
                return "Hello, " + input + "！";
            }
        });
        ActionUtils.registerAction("Hello1", new AbstractAction() {
            @Override
            public Object execute(String strategy, Object input, Object[] arguments) {
                return "[" + strategy + "] Hello, " + input + "！";
            }
        });
    }

    @Test
    public void test1() {
        System.out.println(ActionUtils.execute(actionName, "Action Tools"));
        System.out.println(ActionUtils.execute(actionName, new Object[]{null, "Action Tools 1"}));
    }

    @Test
    public void test2() {
        String execute = ActionUtils.execute("Hello1.fun1", "Action Tools");
        System.out.println(execute);
        execute = ActionUtils.execute("Hello1.fun2", "Action Tools");
        System.out.println(execute);
    }

}
