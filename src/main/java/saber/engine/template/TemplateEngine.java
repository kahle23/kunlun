package saber.engine.template;

import java.io.Reader;

public interface TemplateEngine<T> {

    TemplateEngine clearTemplateCache() throws Exception;

    Template getTemplate(String templateName) throws Exception;

    Template getTemplate(freemarker.template.Template template) throws Exception;

    Template getTemplate(String templateName, String encoding) throws Exception;

    Template getTemplateByString(String templateSource) throws Exception;

    Template getTemplateByString(String templateName, String templateSource) throws Exception;

    Template getTemplateByString(String templateName, String templateSource, boolean cache) throws Exception;

    Template getTemplateByReader(Reader reader) throws Exception;

    Template getTemplateByReader(String templateName, Reader reader) throws Exception;

    Template getTemplateByReader(String templateName, Reader reader, boolean cache) throws Exception;

}
