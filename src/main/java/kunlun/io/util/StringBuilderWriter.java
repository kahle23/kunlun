/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.util;

import java.io.Serializable;
import java.io.Writer;

/**
 * The string writer who using StringBuilder.
 * @author Kahle
 */
public class StringBuilderWriter extends Writer implements Serializable {
    private final StringBuilder builder;

    public StringBuilderWriter() {

        this.builder = new StringBuilder();
    }

    public StringBuilderWriter(int capacity) {

        this.builder = new StringBuilder(capacity);
    }

    public StringBuilderWriter(StringBuilder builder) {

        this.builder = builder != null ? builder : new StringBuilder();
    }

    public StringBuilder getBuilder() {

        return builder;
    }

    @Override
    public void write(String value) {
        if (value != null) {
            builder.append(value);
        }
    }

    @Override
    public void write(char[] value, int offset, int length) {
        if (value != null) {
            builder.append(value, offset, length);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {

        return builder.toString();
    }

}
