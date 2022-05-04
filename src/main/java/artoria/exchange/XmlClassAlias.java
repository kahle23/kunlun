package artoria.exchange;

/**
 * Xml class alias.
 * @author Kahle
 */
public class XmlClassAlias implements XmlFeature {
    private Class<?> type;
    private String alias;

    public XmlClassAlias() {

    }

    public XmlClassAlias(String alias, Class<?> type) {
        this.alias = alias;
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

}
