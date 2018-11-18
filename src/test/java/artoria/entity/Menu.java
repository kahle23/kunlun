package artoria.entity;

import java.io.Serializable;
import java.util.List;

/**
 * one module more menu
 * one menu more module
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private String module;
    private Integer id;
    private String code;
    private String name;
    private String parentCode;
    private List<Menu> sonMenuList;

    public String getModule() {

        return this.module;
    }

    public void setModule(String module) {

        this.module = module;
    }

    public Integer getId() {

        return this.id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCode() {

        return this.code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getParentCode() {

        return this.parentCode;
    }

    public void setParentCode(String parentCode) {

        this.parentCode = parentCode;
    }

    public List<Menu> getSonMenuList() {

        return this.sonMenuList;
    }

    public void setSonMenuList(List<Menu> sonMenuList) {

        this.sonMenuList = sonMenuList;
    }

}
