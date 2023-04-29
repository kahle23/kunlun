package artoria.renderer.support;

/**
 * The printf format text renderer.
 * @author Kahle
 */
public class PrintfTextRenderer extends AbstractFormatTextRenderer {

    @Override
    public String render(String template, Object[] arguments) {

        return String.format(template, arguments);
    }

}
