/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.polyglot.support;

import kunlun.exception.ExceptionUtils;
import kunlun.polyglot.PolyglotService;
import kunlun.reflect.ReflectUtils;
import kunlun.util.Assert;
import kunlun.util.ObjectUtils;

import javax.script.*;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import static kunlun.util.ObjectUtils.cast;

/**
 * The simple polyglot execution service base on "javax.script.ScriptEngineManager".
 * @see javax.script.Invocable
 * @see javax.script.Compilable
 * @see javax.script.CompiledScript
 * @author Kahle
 */
public class ScriptEngineService implements PolyglotService {
    private final ScriptEngineManager scriptEngineManager;
    private final Collection<ScriptEngineFactory> factories;

    public ScriptEngineService(ScriptEngineManager scriptEngineManager) {
        Assert.notNull(scriptEngineManager, "Parameter \"scriptEngineManager\" must not null. ");
        this.scriptEngineManager = scriptEngineManager;
        try {
            Field field = ReflectUtils.getField(ScriptEngineManager.class, "engineSpis");
            ReflectUtils.makeAccessible(field);
            this.factories = cast(field.get(scriptEngineManager));
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public ScriptEngineService() {

        this(new ScriptEngineManager());
    }

    public ScriptEngineManager getScriptEngineManager() {

        return scriptEngineManager;
    }

    /**
     * Returns a list whose elements are instances of all the <code>ScriptEngineFactory</code> classes.
     * @return List of all script engine factories.
     */
    public Collection<ScriptEngineFactory> getFactories() {

        return scriptEngineManager.getEngineFactories();
    }

    /**
     * Register the script engine factory.
     * @param factory The script engine factory instance
     */
    public void registerFactory(ScriptEngineFactory factory) {
        Assert.notNull(factory, "Parameter \"factory\" must not null. ");
        factories.add(factory);
    }

    public ScriptEngine getEngine(String name, Object config) {
        // Handle config.
        // The script engine used shared or unique (cache or pool may be used)
        /*boolean shared = false;
        if (config instanceof Map) {
            shared = Dict.of((Map<?, ?>) config).getBoolean("shared", false);
        }*/
        // Get engine.
        ScriptEngine engine = scriptEngineManager.getEngineByName(name);
        if (engine == null) {
            engine = scriptEngineManager.getEngineByExtension(name);
        }
        if (engine == null) {
            engine = scriptEngineManager.getEngineByMimeType(name);
        }
        if (engine == null) {
            throw new IllegalArgumentException(String.format("Script for [%s] not support! ", name));
        }
        return engine;
    }

    /**
     * Looks up and creates a script engine for a given name.
     * (For thread-unsafe implementations, shared ones are not provided)
     * @param name The unique identification of the scripting language
     * @return The script engine to associate with the given name or null
     */
    public ScriptEngine getEngine(String name) {

        return getEngine(name, null);
    }

    /**
     * Compiles the script for later execution.
     * @param name The unique identification of the scripting language
     * @param script The source for the script
     * @param config The config (most scenarios are Map, nullable)
     * @return An instance of a subclass of <code>CompiledScript</code>
     */
    public CompiledScript compile(String name, Object script, Object config) {
        // Parameter validation.
        Assert.isTrue(script instanceof Reader ||
                        script instanceof String,
                "Script type only support Reader or String. ");
        // Get script engine.
        ScriptEngine engine = getEngine(name, config);
        // Can compile?
        if (!(engine instanceof Compilable)) { return null; }
        Compilable compilable = (Compilable) engine;
        // Do compile.
        try {
            if (script instanceof String) {
                return compilable.compile((String) script);
            }
            else {
                return compilable.compile((Reader) script);
            }
        }
        catch (ScriptException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public Object eval(String name, Object source, Object config, Object data) {
        // Parameter validation.
        Assert.isTrue(source instanceof Reader ||
                        source instanceof String,
                "Script type only support Reader or String. ");
        // Get script engine.
        ScriptEngine engine = getEngine(name, config);
        // Convert Map to Bindings.
        if (data instanceof Map && !(data instanceof Bindings)) {
            data = new SimpleBindings(ObjectUtils.<Map<String, Object>>cast(data));
        }
        try {
            if (data instanceof Bindings) {
                // Do eval by Bindings.
                if (source instanceof Reader) {
                    return engine.eval((Reader) source, (Bindings) data);
                }
                else { return engine.eval((String) source, (Bindings) data); }
            }
            else if (data instanceof ScriptContext) {
                // Do eval by ScriptContext.
                if (source instanceof Reader) {
                    return engine.eval((Reader) source, (ScriptContext) data);
                }
                else { return engine.eval((String) source, (ScriptContext) data); }
            }
            else {
                // Unsupported eval.
                throw new UnsupportedOperationException("Script data type is unsupported! ");
            }
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public Object invoke(String name, Object source, Object config, String function, Object... arguments) {
        // Parameter validation.
        Assert.isTrue(source instanceof Reader ||
                        source instanceof String,
                "Script type only support Reader or String. ");
        // Get script engine.
        ScriptEngine engine = getEngine(name, config);
        // Eval script.
        Object eval;
        try {
            if (source instanceof String) {
                eval = engine.eval((String) source);
            }
            else {
                eval = engine.eval((Reader) source);
            }
        }
        catch (ScriptException e) {
            throw ExceptionUtils.wrap(e);
        }
        // Convert result to invocable.
        Invocable invocable;
        if (eval instanceof Invocable) {
            invocable = (Invocable) eval;
        }
        else if(engine instanceof Invocable) {
            invocable = (Invocable) engine;
        }
        else {
            throw new UnsupportedOperationException("Script is not invocable! ");
        }
        // Invoke function.
        try {
            return invocable.invokeFunction(function, arguments);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
