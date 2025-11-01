package kunlun.data.paging;

import java.util.Collection;

/**
 * “序号”填充器.
 * @author Kahle
 */
public interface SerialNumberFiller {

    String getDefaultSerialNumberFieldName();

    void setDefaultSerialNumberFieldName(String defaultSerialNumberFieldName);

    <T> void fill(Collection<T> data, String fieldName, int pageNum, int pageSize);

}
