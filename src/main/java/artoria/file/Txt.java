package artoria.file;

import artoria.io.IOUtils;
import artoria.util.Assert;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Txt file.
 * @author Kahle
 */
public class Txt extends TextFile implements Appendable {
    private final StringBuilder textBuilder = new StringBuilder();

    @Override
    public long read(Reader reader) throws IOException {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        String read = IOUtils.toString(reader);
        textBuilder.setLength(0);
        textBuilder.append(read);
        return read.length();
    }

    @Override
    public void write(Writer writer) throws IOException {
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        writer.write(textBuilder.toString());
        writer.flush();
    }

    @Override
    public Appendable append(char c) {
        textBuilder.append(c);
        return this;
    }

    @Override
    public Appendable append(CharSequence csq) {
        textBuilder.append(csq);
        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) {
        textBuilder.append(csq, start, end);
        return this;
    }

    public Txt clear() {
        textBuilder.setLength(0);
        return this;
    }

}
