package artoria.data.xml;

/**
 * The xml field alias.
 * @author Kahle
 */
public class XmlFieldAlias implements XmlFeature {
    private Class<?> type;
    private String alias;
    private String field;

    public XmlFieldAlias() {

    }

    public XmlFieldAlias(String alias, Class<?> type, String field) {
        this.alias = alias;
        this.field = field;
        this.type = type;
    }

    public String getAlias() {

        return alias;
    }

    public void setAlias(String alias) {

        this.alias = alias;
    }

    public Class<?> getType() {

        return type;
    }

    public void setType(Class<?> type) {

        this.type = type;
    }

    public String getField() {

        return field;
    }

    public void setField(String field) {

        this.field = field;
    }

}
