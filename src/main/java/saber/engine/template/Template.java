package saber.engine.template;

import java.io.Writer;
import java.util.Map;

/**
 * @author Kahle
 */
public interface Template<T> {

    /**
     * get template
     * @return other type template
     */
    T getTemplate();

    /**
     * set template
     * @param template other type template
     * @return oneself
     */
    Template setTemplate(T template);

    /**
     * data and template render to writer
     * @param data template data
     * @param writer render output
     * @return oneself
     * @throws Exception maybe have different exception
     */
    Template render(Map data, Writer writer) throws Exception;

    /**
     * template render to writer
     * @param writer render output
     * @return oneself
     * @throws Exception maybe have different exception
     */
    Template render(Writer writer) throws Exception;

    /**
     * data and template render to string
     * @param data template data
     * @return html string
     * @throws Exception maybe have different exception
     */
    String renderToString(Map data) throws Exception;

    /**
     * data and template render to string builder
     * @param data template data
     * @return html string builder
     * @throws Exception maybe have different exception
     */
    StringBuilder renderToStringBuilder(Map data) throws Exception;

}
