package saber.engine.template;

import java.io.Writer;
import java.util.Map;

public interface Template<T> {

    T getTemplate();

    Template setTemplate(T template);

    Template render(Map data, Writer writer) throws Exception;

    Template render(Writer writer) throws Exception;

    String renderToString(Map data) throws Exception;

    StringBuilder renderToStringBuilder(Map data) throws Exception;

}
