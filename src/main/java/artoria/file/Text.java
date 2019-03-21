package artoria.file;

import artoria.io.IOUtils;
import artoria.util.Assert;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * Text file.
 * @author Kahle
 */
public class Text extends TextFile {
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

    public Text append(Object obj) {
        textBuilder.append(obj != null ? obj : EMPTY_STRING);
        return this;
    }

    public Text append(Object obj, int start, int end) {
        String input = obj != null ? obj.toString() : EMPTY_STRING;
        textBuilder.append(input, start, end);
        return this;
    }

    public Text clear() {
        textBuilder.setLength(0);
        return this;
    }

}
