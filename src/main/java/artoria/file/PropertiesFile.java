package artoria.file;

import artoria.io.IOUtils;
import artoria.util.Assert;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Properties;

public class PropertiesFile extends TextFile {
    private Properties properties;

    public PropertiesFile() {

        this.properties = new Properties();
    }

    public Properties getProperties() {

        return this.properties;
    }

    public void setProperties(Properties properties) {

        this.properties = properties;
    }

    @Override
    public long read(Reader reader) throws IOException {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        String data = IOUtils.toString(reader);
        properties.load(new StringReader(data));
        return data.length();
    }

    @Override
    public void write(Writer writer) throws IOException {
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        properties.store(writer, null);
    }

}
