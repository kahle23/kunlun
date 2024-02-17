/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.code.support.java;

import kunlun.generator.code.FileContext;
import kunlun.util.MapUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleFileContext implements FileContext {
    private final Map<String, Map<String, Object>> attributesMap = new HashMap<String, Map<String, Object>>();
    private final Map<String, Map<String, String>> outputPathMap = new HashMap<String, Map<String, String>>();
    private final Map<String, Map<String, Object>> tableInfoMap = new HashMap<String, Map<String, Object>>();
    private List<String> tableNames;

    @Override
    public List<String> getTableNames() {

        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {

        this.tableNames = tableNames;
    }

    @Override
    public Map<String, Object> getTableInfo(String tableName) {

        return tableInfoMap.get(tableName);
    }

    public void setTableInfo(String tableName, Map<String, Object> tableInfo) {

        tableInfoMap.put(tableName, tableInfo);
    }

    @Override
    public boolean skipExisted(String builderName) {
        Boolean skipExisted = (Boolean) getAttribute(builderName, "skipExisted");
        return skipExisted != null ? skipExisted : false;
    }

    public void setSkipExisted(String builderName, boolean skipExisted) {

        putAttribute(builderName, "skipExisted", skipExisted);
    }

    @Override
    public Map<String, Object> getAttributes(String builderName) {

        return attributesMap.get(builderName);
    }

    public void putAttributes(String builderName, Map<String, Object> attributes) {
        Map<String, Object> attrs = attributesMap.get(builderName);
        if (attrs == null) {
            attrs = new LinkedHashMap<String, Object>();
            attributesMap.put(builderName, attrs);
        }
        attrs.putAll(attributes);
    }

    @Override
    public Object getAttribute(String builderName, String attrName) {
        Map<String, Object> attributes = getAttributes(builderName);
        if (MapUtils.isEmpty(attributes)) { return null; }
        return attributes.get(attrName);
    }

    @Override
    public void removeAttribute(String builderName, String attrName) {
        Map<String, Object> attributes = getAttributes(builderName);
        if (MapUtils.isEmpty(attributes)) { return; }
        attributes.remove(attrName);
    }

    @Override
    public void putAttribute(String builderName, String attrName, Object attrValue) {
        Map<String, Object> attrs = getAttributes(builderName);
        if (attrs == null) {
            attrs = new LinkedHashMap<String, Object>();
            attributesMap.put(builderName, attrs);
        }
        attrs.put(attrName, attrValue);
    }

    @Override
    public String getTemplateCharset(String builderName) {

        return (String) getAttribute(builderName, "templateCharset");
    }

    public void setTemplateCharset(String builderName, String templateCharset) {

        putAttribute(builderName, "templateCharset", templateCharset);
    }

    @Override
    public String getTemplatePath(String builderName) {

        return (String) getAttribute(builderName, "templatePath");
    }

    public void setTemplatePath(String builderName, String templatePath) {

        putAttribute(builderName, "templatePath", templatePath);
    }

    @Override
    public String getOutputCharset(String builderName) {

        return (String) getAttribute(builderName, "outputCharset");
    }

    public void setOutputCharset(String builderName, String outputCharset) {

        putAttribute(builderName, "outputCharset", outputCharset);
    }

    @Override
    public String getOutputPath(String builderName, String tableName) {
        Map<String, String> tablePathMap = outputPathMap.get(builderName);
        if (MapUtils.isEmpty(tablePathMap)) { return null; }
        return tablePathMap.get(tableName);
    }

    public void setOutputPath(String builderName, String tableName, String outputPath) {
        Map<String, String> tablePathMap = outputPathMap.get(builderName);
        if (tablePathMap == null) {
            tablePathMap = new LinkedHashMap<String, String>();
            outputPathMap.put(builderName, tablePathMap);
        }
        tablePathMap.put(tableName, outputPath);
    }

}
