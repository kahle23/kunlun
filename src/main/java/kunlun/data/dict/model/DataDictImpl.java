/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict.model;

import kunlun.data.dict.DataDict;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The data dictionary implementation.
 * @author Kahle
 */
public class DataDictImpl implements DataDict, Serializable {
    private String namespace;
    private String groupName;
    private String groupCode;
    private String name;
    private String code;
    private String value;
    private String description;
    private Integer sort;
    private Map<String, Object> properties;

    public DataDictImpl() {

        setProperties(new LinkedHashMap<String, Object>());
    }

    @Override
    public String getNamespace() {

        return namespace;
    }

    public void setNamespace(String namespace) {

        this.namespace = namespace;
    }

    @Override
    public String getGroupName() {

        return groupName;
    }

    public void setGroupName(String groupName) {

        this.groupName = groupName;
    }

    @Override
    public String getGroupCode() {

        return groupCode;
    }

    public void setGroupCode(String groupCode) {

        this.groupCode = groupCode;
    }

    @Override
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    @Override
    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    @Override
    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    @Override
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public Integer getSort() {

        return sort;
    }

    public void setSort(Integer sort) {

        this.sort = sort;
    }

    @Override
    public Map<String, Object> getProperties() {

        return properties;
    }

    public void setProperties(Map<String, Object> properties) {

        this.properties = properties;
    }
}
