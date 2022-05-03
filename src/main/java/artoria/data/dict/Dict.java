package artoria.data.dict;

/**
 * The data dictionary object.
 * What is a data dictionary? A list that explains and describes specific information.
 * @author Kahle
 */
public interface Dict {

    /**
     * Get the group information of the dictionary item.
     * @return The group information of the dictionary item
     */
    String getGroup();

    /**
     * Get the name of the dictionary item.
     * @return The name of the dictionary item
     */
    String getName();

    /**
     * Get the code of the dictionary item.
     * @return The code of the dictionary item
     */
    String getCode();

    /**
     * Get the value of the dictionary item.
     * @return The value of the dictionary item
     */
    String getValue();

    /**
     * Get the description of the dictionary item.
     * @return The description of the dictionary item
     */
    String getDescription();

    /**
     * Get the sort of the dictionary item.
     * @return The sort of the dictionary item
     */
    Integer getSort();

}
