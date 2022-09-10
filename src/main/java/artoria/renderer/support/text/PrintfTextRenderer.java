package artoria.renderer.support.text;

public class PrintfTextRenderer extends AbstractFormatTextRenderer {

    @Override
    public String render(String template, Object[] arguments) {

        return String.format(template, arguments);
    }

}
