package artoria.test.pojo.entity.other;

import java.util.List;
import java.util.Map;

/**
 * The nested (test bean).
 * @author Kahle
 */
public class Nested {
    private Long   id;
    private String name;
    private String description;
    private Nested nested;
    private Nested[] nestedArray;
    private List<Nested> nestedList;
    private Map<String, Nested> nestedMap;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Nested getNested() {

        return nested;
    }

    public void setNested(Nested nested) {

        this.nested = nested;
    }

    public Nested[] getNestedArray() {

        return nestedArray;
    }

    public void setNestedArray(Nested[] nestedArray) {

        this.nestedArray = nestedArray;
    }

    public List<Nested> getNestedList() {

        return nestedList;
    }

    public void setNestedList(List<Nested> nestedList) {

        this.nestedList = nestedList;
    }

    public Map<String, Nested> getNestedMap() {

        return nestedMap;
    }

    public void setNestedMap(Map<String, Nested> nestedMap) {

        this.nestedMap = nestedMap;
    }

}
