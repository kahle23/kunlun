package artoria.io;

import java.io.Serializable;
import java.io.Writer;

/**
 * The StringWriter who using StringBuilder.
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

        return this.builder;
    }

    @Override
    public void write(String value) {
        if (value != null) {
            this.builder.append(value);
        }
    }

    @Override
    public void write(char[] value, int offset, int length) {
        if (value != null) {
            this.builder.append(value, offset, length);
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

        return this.builder.toString();
    }

}
