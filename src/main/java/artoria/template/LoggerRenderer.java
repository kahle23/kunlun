package artoria.template;

import artoria.exception.ExceptionUtils;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.io.Writer;

/**
 * Log parameters renderer.
 * @author Kahle
 */
public class LoggerRenderer implements Renderer {
    private static final char ESCAPE_SYMBOL = '\\';
    private static final String PLACEHOLDER = "{}";
    private static final Integer PLACEHOLDER_LENGTH = PLACEHOLDER.length();

    @Override
    public void render(Object data, Object output, String name, Object input, String charsetName) throws RenderException {
        try {
            Assert.state(output instanceof Writer, "Parameter \"output\" must instance of \"Writer\". ");
            Assert.state(input == null || input instanceof String
                    , "Parameter \"input\" must instance of \"String\". ");
            Assert.state(data == null || data instanceof Object[]
                    , "Parameter \"data\" must instance of \"Object[]\". ");
            Writer writer = (Writer) output;
            String format = (String) input;
            Object[] args = (Object[]) data;
            if (StringUtils.isBlank(format) || ArrayUtils.isEmpty(args)) {
                writer.write(String.valueOf(format));
                return;
            }
            int index, start = 0, count = 0, argsLen = args.length, iTmp;
            while ((index = format.indexOf(PLACEHOLDER, start)) != -1) {
                boolean bTmp = (iTmp = index - 2) < 0
                        || ESCAPE_SYMBOL != format.charAt(iTmp);
                bTmp = bTmp && (iTmp = index - 1) > 0;
                if (bTmp && ESCAPE_SYMBOL == format.charAt(iTmp)) {
                    writer.write(format.substring(start, iTmp));
                    writer.write(PLACEHOLDER);
                    start = index + PLACEHOLDER_LENGTH;
                    continue;
                }
                writer.write(format.substring(start, index));
                writer.write(String.valueOf(count < argsLen ? args[count++] : PLACEHOLDER));
                start = index + PLACEHOLDER_LENGTH;
            }
            if (start < format.length()) { writer.write(format.substring(start)); }
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e, RenderException.class);
        }
    }

}
