package artoria.engine.template;

public class PrintfTemplateEngine extends AbstractPlainTemplateEngine {

    @Override
    public String render(String template, Object[] arguments) {

        return String.format(template, arguments);
    }

}
