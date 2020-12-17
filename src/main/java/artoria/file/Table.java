package artoria.file;

import artoria.data.MapListable;

import java.util.List;
import java.util.Map;

/**
 * Abstract table.
 * @author Kahle
 */
public interface Table extends MapListable {

    /**
     * Get last row number.
     * @return Last row number
     */
    int getLastRowNumber();

    /**
     * Get last cell number.
     * @param rowNumber Row number
     * @return Last cell number
     */
    int getLastCellNumber(int rowNumber);

    /**
     * Get row content by row number.
     * @param rowNumber Row number
     * @return Row content
     */
    List<Object> getRowContent(int rowNumber);

    /**
     * Set row content by row number.
     * @param rowNumber Row number
     * @param rowContent Row content will be set
     */
    void setRowContent(int rowNumber, List<?> rowContent);

    /**
     * Get cell content by row number and column number.
     * @param rowNumber Row number
     * @param columnNumber Column number
     * @return Cell content
     */
    Object getCellContent(int rowNumber, int columnNumber);

    /**
     * Set cell content by row number and column number.
     * @param rowNumber Row number
     * @param columnNumber Column number
     * @param cellContent Cell content will be set
     */
    void setCellContent(int rowNumber, int columnNumber, Object cellContent);

    /**
     * Get row start number.
     * @return Row start number
     */
    int getRowStartNumber();

    /**
     * Set row start number.
     * @param rowStartNumber Row start number will be set
     */
    void setRowStartNumber(int rowStartNumber);

    /**
     * Get column start number.
     * @return Column start number
     */
    int getColumnStartNumber();

    /**
     * Set column start number.
     * @param columnStartNumber Column start number will be set
     */
    void setColumnStartNumber(int columnStartNumber);

    /**
     * Get template.
     * @return Template data
     */
    byte[] getTemplate();

    /**
     * Set template and the style of the template will be inherited.
     * @param template Template data
     */
    void setTemplate(byte[] template);

    /**
     * Add header mapping.
     * @param headerName Header name will be add
     * @param propertyName Property name will be add
     */
    void addHeader(String headerName, String propertyName);

    /**
     * Add headers mapping.
     * @param headers Headers mapping will be add
     */
    void addHeaders(Map<?, ?> headers);

    /**
     * Remove header by header name.
     * @param headerName Header name
     */
    void removeHeaderByHeaderName(String headerName);

    /**
     * Remove header by property name.
     * @param propertyName Property name
     */
    void removeHeaderByPropertyName(String propertyName);

    /**
     * Clear headers.
     */
    void clearHeaders();

}
