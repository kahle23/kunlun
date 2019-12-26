package artoria.data;

import java.io.Serializable;

/**
 * Provide access to raw data.
 * @author Kahle
 */
public interface RawData extends Serializable {

    /**
     * Get raw data object.
     * @return Raw data object
     */
    Object rawData();

    /**
     * Set raw data object.
     * @param rawData Raw data object
     */
    void rawData(Object rawData);

}
