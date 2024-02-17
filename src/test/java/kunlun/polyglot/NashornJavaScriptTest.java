/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.polyglot;

import kunlun.data.Dict;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.polyglot.support.ScriptEngineProvider;
import kunlun.util.ObjectUtils;
import org.junit.Test;

import javax.script.*;

import static org.junit.Assert.*;

/**
 * The polyglot execution tools Test (nashorn).
 * @author Kahle
 * @see jdk.nashorn.api.scripting.NashornScriptEngineFactory
 * @see jdk.nashorn.api.scripting.NashornScriptEngine
 */
public class NashornJavaScriptTest {
    private static final Logger log = LoggerFactory.getLogger(NashornJavaScriptTest.class);
    private final String name = "nashorn";

    @Test
    public void test1() {
        String script = "a = 2; b = 3;\n" +
                "var c = a + b;\n" +
                "c;";
        Dict data = Dict.of("a", 1).set("b", 2);
        Object result = PolyglotUtils.eval(name, script, data);
        log.info("result: {}", result);
        // Assert.
        assertTrue("a = 2; b = 3; a + b should be 5.0", ObjectUtils.equals(result, 5D));
        assertSame("data must not updated", 1, data.get("a"));
        assertSame("data must not updated", 2, data.get("b"));
    }

    @Test
    public void test2() {
        String script = "a.b";
        Dict data = Dict.of("a", Dict.of("b", 4));
        Object result = PolyglotUtils.eval(name, script, data);
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 4));
    }

    @Test
    public void test3() {
        String script = "var System = Java.type(\"java.lang.System\");\n" +
                "var time = System.currentTimeMillis();\n" +
                "time;";
        Dict data = Dict.of();
        Object result = PolyglotUtils.eval(name, script, data);
        log.info("result: {}", result);
        assertNotNull(result);
    }

    @Test
    public void test4() {
        // Since there is no cache, two calls are two engines.
        // If you want to pass variables,
        //      you can only pass them through the same context and set the attributes to be global.
        ScriptContext context = new SimpleScriptContext();
        context.setBindings(new SimpleBindings(), ScriptContext.GLOBAL_SCOPE);
        context.setAttribute("e", 5, ScriptContext.GLOBAL_SCOPE);
        String script; Object result;

        script = "var a = 1; var b = 2; a + b + e;";
        result = PolyglotUtils.eval(name, script, context);
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 8D));

        script = "var c = 3; var d = 4; c + d + e;";
        result = PolyglotUtils.eval(name, script, context);
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 12D));
    }

    @Test
    public void test5() {
        String script = "function test(arg1, arg2, arg3) {\n" +
                "return arg1 + arg2 + arg3;\n" +
                "}\n";
        Object result = PolyglotUtils.invoke(name, script, "test", 1, 2, 3);
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 6D));
    }

    @Test
    public void test6() {
        String script = "function test(arg1, arg2, arg3) {\n" +
                "return arg1 + arg2 + arg3;\n" +
                "}\n";
        script += "function test1(arg1, arg2) {\n" +
                "return arg1 * arg2;\n" +
                "}\n";
        script += "var a = 1, b = 2, c = 3;\n" +
                "test(a, b, c) + test1(b, c);";
        Dict data = Dict.of();
        Object result = PolyglotUtils.eval(name, script, data);
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 12D));
    }

    @Test
    public void test7() throws ScriptException {
        String script = "function test(arg1, arg2, arg3) {\n" +
                "return arg1 + arg2 + arg3;\n" +
                "}\n";
        script += "function test1(arg1, arg2) {\n" +
                "return arg1 * arg2;\n" +
                "}\n";
        script += "var a = 1, b = 2, c = 3;\n" +
                "test(a, b, c) + test1(b, c);";
        ScriptEngineProvider provider = (ScriptEngineProvider) PolyglotUtils.getPolyglotProvider();
        CompiledScript compiledScript = provider.compile(name, script, null);
        Object result = compiledScript.eval(new SimpleBindings());
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 12D));
    }

}
