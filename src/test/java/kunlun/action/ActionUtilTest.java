/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.core.Action;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

/**
 * The action tools Test.
 * @author Kahle
 */
public class ActionUtilTest {
    private static final Logger log = LoggerFactory.getLogger(ActionUtilTest.class);
    private static final String actionName = "Hello";

    static {
        ActionUtil.registerAction(actionName, new Action() {
            @Override
            public Object execute(String strategy, Object input, Object[] arguments) {
                // \u000dSystem.out.println("Hello, World! ");
                return "Hello, " + input + "！";
            }
        });
        ActionUtil.registerAction("Hello1", new Action() {
            @Override
            public Object execute(String strategy, Object input, Object[] arguments) {
                return "[" + strategy + "] Hello, " + input + "！";
            }
        });
        ActionUtil.registerShortcut(String.class, "Hello1");
    }

    @Test
    public void test1() {
        System.out.println(ActionUtil.execute(actionName, "Action Tools"));
        System.out.println(ActionUtil.execute("Action Tools 1"));
    }

    @Test
    public void test2() {
        String execute = ActionUtil.execute("Hello1.fun1", "Action Tools");
        System.out.println(execute);
        execute = ActionUtil.execute("Hello1.fun2", "Action Tools");
        System.out.println(execute);
    }

}
