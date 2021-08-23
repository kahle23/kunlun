package artoria.engine.template;

public interface PlainTemplateEngine extends StringTemplateEngine {

    String render(String template, Object[] arguments);

}
