package artoria.file;

import artoria.beans.BeanUtils;
import artoria.io.IOUtils;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

import static artoria.common.Constants.*;

/**
 * Csv file.
 * @author Kahle
 */
public class Csv extends TextFile implements Table {
    private final Map<String, String> propertiesMapping = new LinkedHashMap<String, String>();
    private final Map<String, String> headersMapping = new LinkedHashMap<String, String>();
    private final List<List<String>> content = new ArrayList<List<String>>();
    private String lineSeparator = NEWLINE;
    private String cellSeparator = COMMA;
    private int columnStartNumber = 0;
    private int rowStartNumber = 0;

    public String getLineSeparator() {

        return lineSeparator;
    }

    public void setLineSeparator(String lineSeparator) {
        Assert.notNull(lineSeparator
                , "Parameter \"lineSeparator\" must not null. ");
        lineSeparator = StringUtils.replace(lineSeparator, BLANK_SPACE, EMPTY_STRING);
        Assert.notEmpty(lineSeparator
                , "Parameter \"lineSeparator\" cannot be blank space only. ");
        this.lineSeparator = lineSeparator;
    }

    public String getCellSeparator() {

        return cellSeparator;
    }

    public void setCellSeparator(String cellSeparator) {
        Assert.notBlank(cellSeparator, "Parameter \"cellSeparator\" must not blank. ");
        Assert.state(cellSeparator.length() == 1
                , "Parameter \"cellSeparator\" must be a single character. ");
        Assert.state(DOUBLE_QUOTE.equals(cellSeparator)
                , "Parameter \"cellSeparator\" must not equal double quotes. ");
        Assert.state(lineSeparator.equals(cellSeparator)
                , "Parameter \"cellSeparator\" must not equal line separator. ");
        this.cellSeparator = cellSeparator;
    }

    @Override
    public long read(Reader reader) throws IOException {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        content.clear();
        String text = IOUtils.toString(reader);
        int textLength = text.length();
        if (textLength == 0) { return 0; }
        int fromIndex = 0, quoteIndex, lineIndex, index;
        List<String> row = new ArrayList<String>();
        for (; ; ) {
            boolean haveQuote = false;
            quoteIndex = text.indexOf(DOUBLE_QUOTE, fromIndex);
            if (quoteIndex != -1
                    && StringUtils.isBlank(text.substring(fromIndex, quoteIndex))) {
                haveQuote = true;
                fromIndex = quoteIndex + 1;
                index = fromIndex;
                boolean loop = true;
                while (loop) {
                    quoteIndex = text.indexOf(DOUBLE_QUOTE, index);
                    if (quoteIndex == -1) { index = -1; break; }
                    index = quoteIndex + 1;
                    char nextChar = text.charAt(index);
                    if (DOUBLE_QUOTE.equals(nextChar + EMPTY_STRING)) {
                        index++; loop = true;
                    }
                    else {
                        index = text.indexOf(cellSeparator, index); loop = false;
                    }
                }
            }
            else { index = text.indexOf(cellSeparator, fromIndex); }
            if (index == -1) {
                row.add(text.substring(fromIndex));
                content.add(row); break;
            }
            lineIndex = text.indexOf(lineSeparator, fromIndex);
            if (!haveQuote && lineIndex < index) {
                String tmpStr = text.substring(fromIndex, lineIndex);
                if (StringUtils.isNotBlank(tmpStr)) { row.add(tmpStr); }
                fromIndex = lineIndex + lineSeparator.length();
                content.add(row); row = new ArrayList<String>();
                continue;
            }
            String tmpStr = text.substring(fromIndex, index);
            if (haveQuote) {
                int tmpIndex = quoteIndex - fromIndex;
                String tmpBegin = tmpStr.substring(0, tmpIndex);
                tmpBegin = StringUtils.replace(tmpBegin, "\"\"", DOUBLE_QUOTE);
                tmpStr = tmpBegin + tmpStr.substring(tmpIndex + 1, tmpStr.length());
            }
            row.add(tmpStr);
            fromIndex = index + 1;
        }
        return textLength;
    }

    @Override
    public void write(Writer writer) throws IOException {
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        if (CollectionUtils.isEmpty(content)) { return; }
        StringBuilder builder = new StringBuilder();
        for (List<String> row : content) {
            if (row == null) { continue; }
            for (String cell : row) {
                if (StringUtils.isBlank(cell)) {
                    builder.append(cell).append(cellSeparator);
                    continue;
                }
                boolean needQuote = cell.contains(cellSeparator);
                needQuote = needQuote || cell.contains(lineSeparator);
                boolean containQuote = cell.contains(DOUBLE_QUOTE);
                cell = needQuote && containQuote
                        ? StringUtils.replace(cell, DOUBLE_QUOTE, "\"\"")
                        : cell;
                cell = needQuote
                        ? DOUBLE_QUOTE + cell + DOUBLE_QUOTE
                        : cell;
                builder.append(cell).append(cellSeparator);
            }
            builder.append(lineSeparator);
        }
        writer.write(builder.toString());
        writer.flush();
    }

    @Override
    public int getLastRowNumber() {

        return content.size();
    }

    @Override
    public int getLastCellNumber(int rowNumber) {
        List<?> rowContent = this.getRowContent(rowNumber);
        return rowContent != null ? rowContent.size() : 0;
    }

    @Override
    public List<Object> getRowContent(int rowNumber) {
        int lastRowNumber = this.getLastRowNumber();
        Assert.state(rowNumber > 0 && rowNumber <= lastRowNumber
                , "Parameter \"rowNumber\" must > 0 and <= last row number. ");
        List<String> row = content.get(rowNumber - 1);
        List<Object> result = new ArrayList<Object>();
        if (row == null) { return result; }
        result.addAll(row);
        return result;
    }

    @Override
    public void setRowContent(int rowNumber, List<?> rowContent) {
        Assert.state(rowNumber > 0, "Parameter \"rowNumber\" must > 0. ");
        Assert.notNull(rowContent, "Parameter \"rowContent\" must not null. ");
        int lastRowNumber = this.getLastRowNumber();
        if (rowNumber > lastRowNumber) {
            for (int i = lastRowNumber; i <= rowNumber; i++) {
                content.add(new ArrayList<String>());
            }
        }
        List<String> row = new ArrayList<String>();
        for (Object cell : rowContent) {
            row.add(cell != null ? cell.toString() : EMPTY_STRING);
        }
        content.set(rowNumber - 1, row);
    }

    @Override
    public Object getCellContent(int rowNumber, int columnNumber) {
        int lastCellNumber = this.getLastCellNumber(rowNumber);
        Assert.state(columnNumber > 0 && columnNumber <= lastCellNumber
                , "Parameter \"columnNumber\" must > 0 and <= last cell number. ");
        List<Object> rowContent = this.getRowContent(rowNumber);
        return rowContent != null ? rowContent.get(columnNumber - 1) : null;
    }

    @Override
    public void setCellContent(int rowNumber, int columnNumber, Object cellContent) {
        Assert.state(rowNumber > 0, "Parameter \"rowNumber\" must > 0. ");
        Assert.state(columnNumber > 0, "Parameter \"columnNumber\" must > 0. ");
        Assert.notNull(cellContent, "Parameter \"cellContent\" must not null. ");
        List<String> row;
        int lastRowNumber = this.getLastRowNumber();
        if (rowNumber > lastRowNumber
                || (row = content.get(rowNumber - 1)) == null) {
            row = new ArrayList<String>();
            this.setRowContent(rowNumber, row);
        }
        int rowSize = row.size();
        if (columnNumber > rowSize) {
            for (int i = rowSize; i <= columnNumber; i++) {
                row.add(EMPTY_STRING);
            }
        }
        row.set(columnNumber - 1, cellContent.toString());
    }

    @Override
    public int getRowStartNumber() {

        return rowStartNumber;
    }

    @Override
    public void setRowStartNumber(int rowStartNumber) {
        Assert.state(rowStartNumber >= 0
                , "Parameter \"rowStartNumber\" must >= 0. ");
        this.rowStartNumber = rowStartNumber;
    }

    @Override
    public int getColumnStartNumber() {

        return columnStartNumber;
    }

    @Override
    public void setColumnStartNumber(int columnStartNumber) {
        Assert.state(columnStartNumber >= 0
                , "Parameter \"columnStartNumber\" must >= 0. ");
        this.columnStartNumber = columnStartNumber;
    }

    @Override
    public byte[] getTemplate() {

        return null;
    }

    @Override
    public void setTemplate(byte[] template) {
    }

    @Override
    public void addHeader(String headerName, String propertyName) {
        Assert.notBlank(propertyName
                , "Parameter \"propertyName\" must not blank. ");
        Assert.notBlank(headerName
                , "Parameter \"headerName\" must not blank. ");
        this.propertiesMapping.put(propertyName, headerName);
        this.headersMapping.put(headerName, propertyName);
    }

    @Override
    public void addHeaders(Map<?, ?> headers) {
        Assert.notEmpty(headers
                , "Parameter \"headers\" must not empty. ");
        for (Map.Entry<?, ?> entry : headers.entrySet()) {
            String key = entry.getKey() != null
                    ? entry.getKey().toString() : EMPTY_STRING;
            String val = entry.getValue() != null
                    ? entry.getValue().toString() : EMPTY_STRING;
            this.propertiesMapping.put(val, key);
            this.headersMapping.put(key, val);
        }
    }

    @Override
    public void removeHeaderByHeaderName(String headerName) {
        Assert.notNull(headerName
                , "Parameter \"headerName\" must not null. ");
        if (!headersMapping.containsKey(headerName)) { return; }
        String propertyName = this.headersMapping.get(headerName);
        this.propertiesMapping.remove(propertyName);
        this.headersMapping.remove(headerName);
    }

    @Override
    public void removeHeaderByPropertyName(String propertyName) {
        Assert.notNull(propertyName
                , "Parameter \"propertyName\" must not null. ");
        if (!propertiesMapping.containsKey(propertyName)) { return; }
        String headerName = this.propertiesMapping.get(propertyName);
        this.propertiesMapping.remove(propertyName);
        this.headersMapping.remove(headerName);
    }

    @Override
    public void clearHeaders() {
        this.headersMapping.clear();
        this.propertiesMapping.clear();
    }

    @Override
    public <T> List<T> toBeanList(Class<T> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        List<Map<String, Object>> mapList = this.toMapList();
        return BeanUtils.mapToBeanInList(mapList, clazz);
    }

    @Override
    public <T> void fromBeanList(List<T> beanList) {
        Assert.notEmpty(beanList, "Parameter \"beanList\" must not empty. ");
        List<Map<String, Object>> mapList = BeanUtils.beanToMapInList(beanList);
        this.fromMapList(mapList);
    }

    @Override
    public List<Map<String, Object>> toMapList() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isEmpty(content)) { return result; }
        boolean haveHeaders = MapUtils.isNotEmpty(headersMapping);
        List<String> propertyList = new ArrayList<String>();
        boolean isFirst = true;
        for (int i = columnStartNumber, cLen = content.size(); i < cLen; i++) {
            List<String> row = content.get(i);
            if (CollectionUtils.isEmpty(row)) { continue; }
            if (isFirst) {
                for (String cell : row) {
                    cell = StringUtils.isBlank(cell) ? EMPTY_STRING : cell;
                    String property = haveHeaders ? headersMapping.get(cell) : cell;
                    property = StringUtils.isNotBlank(property) ? property : cell;
                    propertyList.add(property);
                }
                isFirst = false;
                continue;
            }
            int pLen = propertyList.size(), rowSize = row.size();
            Map<String, Object> map = new HashMap<String, Object>(pLen);
            for (int j = rowStartNumber; j < pLen; j++) {
                String cell = j < rowSize ? row.get(j) : null;
                if (StringUtils.isBlank(cell)) { cell = null; }
                String key = propertyList.get(j);
                map.put(key, cell);
            }
            result.add(map);
        }
        return result;
    }

    @Override
    public void fromMapList(List<Map<String, Object>> mapList) {
        Assert.notEmpty(mapList, "Parameter \"mapList\" must not empty. ");
        content.clear();
        List<String> headerList = new ArrayList<String>();
        if (rowStartNumber != 0) {
            for (int i = 0; i < rowStartNumber; i++) {
                headerList.add(EMPTY_STRING);
            }
        }
        boolean haveHeaders = MapUtils.isNotEmpty(propertiesMapping);
        if (haveHeaders) {
            headerList.addAll(propertiesMapping.values());
        }
        else {
            Map<String, Object> first = CollectionUtils.getFirstNotNullElement(mapList);
            headerList.addAll(first.keySet());
        }
        if (columnStartNumber != 0) {
            for (int i = 0; i < columnStartNumber; i++) {
                content.add(new ArrayList<String>());
            }
        }
        content.add(headerList);
        for (Map<String, Object> beanMap : mapList) {
            if (beanMap == null) { continue; }
            List<String> row = new ArrayList<String>();
            if (rowStartNumber != 0) {
                for (int i = 0; i < rowStartNumber; i++) {
                    row.add(EMPTY_STRING);
                }
            }
            if (haveHeaders) {
                for (String property : propertiesMapping.keySet()) {
                    Object val = beanMap.get(property);
                    row.add(val != null ? val.toString() : EMPTY_STRING);
                }
            }
            else {
                for (Object val : beanMap.values()) {
                    row.add(val != null ? val.toString() : EMPTY_STRING);
                }
            }
            content.add(row);
        }
    }

}
