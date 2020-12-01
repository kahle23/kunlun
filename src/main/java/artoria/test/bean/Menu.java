package artoria.test.bean;

import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable {
    private Long   id;
    private Long   parentId;
    private String name;
    private String type;
    private String icon;
    private String description;
    private List<Menu> children;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public Long getParentId() {

        return parentId;
    }

    public void setParentId(Long parentId) {

        this.parentId = parentId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getIcon() {

        return icon;
    }

    public void setIcon(String icon) {

        this.icon = icon;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public List<Menu> getChildren() {

        return children;
    }

    public void setChildren(List<Menu> children) {

        this.children = children;
    }

}
