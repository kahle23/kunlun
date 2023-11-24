package artoria.generator.code;

import artoria.core.Context;

import java.util.List;
import java.util.Map;

public interface FileContext extends Context {

    List<String> getTableNames();

    Map<String, Object> getTableInfo(String tableName);

    boolean skipExisted(String builderName);

    Map<String, Object> getAttributes(String builderName);

    Object getAttribute(String builderName, String attrName);

    void removeAttribute(String builderName, String attrName);

    void putAttribute(String builderName, String attrName, Object attrValue);

    String getTemplateCharset(String builderName);

    String getTemplatePath(String builderName);

    String getOutputCharset(String builderName);

    String getOutputPath(String builderName, String tableName);

}
