package saber.engine.template;

import java.io.Reader;

/**
 * @author Kahle
 */
public interface TemplateEngine<T> {

    /**
     * clear template cache
     * @return oneself
     * @throws Exception maybe have different exception
     */
    TemplateEngine clearTemplateCache() throws Exception;

    /**
     * get template by name
     * @param templateName template name
     * @return template
     * @throws Exception maybe have different exception
     */
    Template getTemplate(String templateName) throws Exception;

    /**
     * get template by freemarker template
     * @param template freemarker template
     * @return template
     * @throws Exception maybe have different exception
     */
    Template getTemplate(freemarker.template.Template template) throws Exception;

    /**
     * get template by name and encoding
     * @param templateName template name
     * @param encoding template encoding
     * @return template
     * @throws Exception maybe have different exception
     */
    Template getTemplate(String templateName, String encoding) throws Exception;

    /**
     * get template by string
     * @param templateSource template source
     * @return template
     * @throws Exception maybe have different exception
     */
    Template getTemplateByString(String templateSource) throws Exception;

    /**
     * get template by name and string
     * @param templateName template name
     * @param templateSource template source
     * @return template
     * @throws Exception maybe have different exception
     */
    Template getTemplateByString(String templateName, String templateSource) throws Exception;

    /**
     * get template by name, string and can cache
     * @param templateName template name
     * @param templateSource template source
     * @param cache put cache?
     * @return template
     * @throws Exception maybe have different exception
     */
    Template getTemplateByString(String templateName, String templateSource, boolean cache) throws Exception;

    /**
     * get template by reader
     * @param reader io reader
     * @return template
     * @throws Exception maybe have different exception
     */
    Template getTemplateByReader(Reader reader) throws Exception;

    /**
     * get template by name and reader
     * @param templateName template name
     * @param reader io reader
     * @return template
     * @throws Exception maybe have different exception
     */
    Template getTemplateByReader(String templateName, Reader reader) throws Exception;

    /**
     * get template by name, reader and can cache
     * @param templateName template name
     * @param reader io reader
     * @param cache put cache?
     * @return template
     * @throws Exception maybe have different exception
     */
    Template getTemplateByReader(String templateName, Reader reader, boolean cache) throws Exception;

}
