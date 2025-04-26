/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

import java.io.Serializable;
import java.util.Map;

/**
 * The data dictionary query condition.
 * @author Kahle
 */
public class DictQuery implements Serializable {
    /**
     * The page number.
     */
    private Integer pageNum;
    /**
     * The page size.
     */
    private Integer pageSize;


    /**
     * The namespace of the dictionary item.
     */
    private String namespace;
    /**
     * The group information of the dictionary item.
     */
    private String groupCode;
    /**
     * The name of the dictionary item.
     */
    private String name;
    /**
     * The code of the dictionary item.
     */
    private String code;
    /**
     * The value of the dictionary item.
     */
    private String value;
    /**
     * The other query properties.
     */
    private Map<String, Object> others;


    public DictQuery(String namespace, String groupCode) {
        this.namespace = namespace;
        this.groupCode = groupCode;
    }

    public DictQuery(String groupCode) {

        this.groupCode = groupCode;
    }

    public DictQuery() {

    }


    public Integer getPageNum() {

        return pageNum;
    }

    public void setPageNum(Integer pageNum) {

        this.pageNum = pageNum;
    }

    public Integer getPageSize() {

        return pageSize;
    }

    public void setPageSize(Integer pageSize) {

        this.pageSize = pageSize;
    }

    public String getNamespace() {

        return namespace;
    }

    public void setNamespace(String namespace) {

        this.namespace = namespace;
    }

    public String getGroupCode() {

        return groupCode;
    }

    public void setGroupCode(String groupCode) {

        this.groupCode = groupCode;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    public Map<String, Object> getOthers() {

        return others;
    }

    public void setOthers(Map<String, Object> others) {

        this.others = others;
    }

}
