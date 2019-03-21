package artoria.template;

import artoria.beans.BeanUtils;
import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;
import artoria.util.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.util.Map;

import static artoria.common.Constants.*;
import static artoria.io.IOUtils.EOF;

/**
 * Template renderer simple implement by jdk.
 * @author Kahle
 */
public class DefaultRenderer implements Renderer {

    protected void render(Object data, Writer writer, String template) throws IOException, ParseException {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        if (StringUtils.isBlank(template)) { return; }
        Map dataMap = data instanceof Map
                ? (Map) data : BeanUtils.beanToMap(data);
        for (int finish = template.length(), begin = 0, end = 0; end != finish; ) {
            end = template.indexOf(DOLLAR_SIGN + LEFT_CURLY_BRACKET, begin);
            end = end == EOF ? finish : end;
            writer.write(template.substring(begin, end));
            if (end == finish) { continue; }
            end = template.indexOf(RIGHT_CURLY_BRACKET, (begin = end + 2));
            if (end == EOF) {
                throw new ParseException("After \"${\" must be \"}\" in index \"" + begin + "\". ", begin);
            }
            Object obj = dataMap.get(template.substring(begin, end));
            writer.write(obj != null ? obj.toString() : EMPTY_STRING);
            begin = ++end;
        }
    }

    @Override
    public void render(Object data, Object output, String name, Object input, String charsetName) throws RenderException {
        try {
            Assert.state(output instanceof Writer, "Parameter \"output\" must instance of \"Writer\". ");
            Assert.notBlank(name, "Parameter \"name\" must not blank. ");
            String template;
            if (input instanceof Reader || input instanceof String) {
                template = input instanceof Reader ? IOUtils.toString((Reader) input) : (String) input;
            }
            else {
                InputStream in = ClassLoaderUtils.getResourceAsStream(name, this.getClass());
                if (in == null) {
                    throw new IOException("Can not find template by \"" + name + "\" in classpath. ");
                }
                charsetName = StringUtils.isNotBlank(charsetName) ? charsetName : DEFAULT_CHARSET_NAME;
                Reader reader = new InputStreamReader(in, charsetName);
                template = IOUtils.toString(reader);
            }
            this.render(data, (Writer) output, template);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e, RenderException.class);
        }
    }

}
