/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.core.Action;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * 带类型声明的 action 输入（Action.Input）的测试.
 * @author Kahle
 */
public class ActionInputTest {

    private static final String ACTION_NAME = "typed-echo";

    /**
     * 返回类型为 String 的输入.
     */
    public static class TextInput implements Action.Input<String> {

        private final String value;

        public TextInput(String value) {

            this.value = value;
        }

        public String getValue() { return value; }

    }

    /**
     * 返回类型为嵌套泛型的输入.
     */
    public static class MapInput implements Action.Input<Map<String, List<String>>> {

    }

    /**
     * 测试用的回显 action（按输入类型决定返回值）.
     */
    public static class EchoAction implements Action {

        @Override
        public Object execute(String strategy, Object input, Object[] arguments) {
            if (input instanceof TextInput) {
                return ((TextInput) input).getValue();
            }
            else if (input instanceof MapInput) {
                Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
                result.put("keys", Arrays.asList("a", "b"));
                return result;
            }
            else if (input instanceof String) {
                return "Hello, " + input + "!";
            }
            return null;
        }

    }

    @BeforeClass
    public static void init() {
        EchoAction action = new EchoAction();
        ActionUtil.registerAction(ACTION_NAME, action);
        ActionUtil.registerShortcut(TextInput.class, ACTION_NAME);
        ActionUtil.registerShortcut(MapInput.class, ACTION_NAME);
    }

    @Test
    public void testManagerLevel() {
        ActionManager actionManager = ActionUtil.getActionManager();
        // manager 的 typed 重载：无显式类型参数、无强转
        String result = actionManager.execute(ACTION_NAME, new TextInput("abc"), null);
        assertEquals("abc", result);
        // 按输入类型的 shortcut 路由（command 为空）
        result = actionManager.execute("", new TextInput("abc"), null);
        assertEquals("abc", result);
    }

    @Test
    public void testUtilCommand() {
        String result = ActionUtil.execute(ACTION_NAME, new TextInput("abc"));
        assertEquals("abc", result);
        // 可变参数进 arguments
        result = ActionUtil.execute(ACTION_NAME, new TextInput("abc"), "arg1", "arg2");
        assertEquals("abc", result);
    }

    @Test
    public void testUtilShortcut() {
        String result = ActionUtil.execute(new TextInput("abc"));
        assertEquals("abc", result);
    }

    @Test
    public void testChaining() {
        // 无赋值目标：链式调用可用（R 推断为 String）
        int length = ActionUtil.execute(new TextInput("abc")).length();
        assertEquals(3, length);
    }

    @Test
    public void testNestedGenericResult() {
        Map<String, List<String>> result = ActionUtil.execute(new MapInput());
        List<String> keys = result.get("keys");
        assertEquals(Arrays.asList("a", "b"), keys);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testRawInputFallsBackToObject() {
        Action.Input input = new TextInput("abc");
        Object result = ActionUtil.execute(input);
        assertEquals("abc", result);
    }

    @Test
    public void testUntypedInputOldOverload() {
        // String 未实现 Action.Input：走老的重载
        String result = ActionUtil.execute(ACTION_NAME, "World");
        assertEquals("Hello, World!", result);
    }

}
