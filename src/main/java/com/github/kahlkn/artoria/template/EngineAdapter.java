package com.github.kahlkn.artoria.template;

import java.io.Reader;
import java.io.Writer;

/**
 * Template engine adapter.
 * @author Kahle
 */
public interface EngineAdapter {

    /**
     * Rendering template by name.
     * @param name Template name
     * @param encoding Template content charset
     * @param data Template filled model
     * @param writer The result will output
     * @throws Exception Have more different exception
     */
    void render(String name, String encoding, Object data, Writer writer) throws Exception;

    /**
     * Rendering template by content.
     * @param data Template filled model
     * @param writer The result will output
     * @param logTag Template name and log messages name
     * @param reader Template input stream
     * @throws Exception Have more different exception
     */
    void render(Object data, Writer writer, String logTag, Reader reader) throws Exception;

}
