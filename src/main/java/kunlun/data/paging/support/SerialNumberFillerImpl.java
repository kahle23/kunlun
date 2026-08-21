package kunlun.data.paging.support;

import kunlun.convert.ConversionUtil;
import kunlun.data.paging.SerialNumberFiller;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.reflect.ReflectUtil;
import kunlun.util.CollUtil;
import kunlun.util.StrUtil;

import java.lang.reflect.Field;
import java.util.Collection;

import static kunlun.util.Assert.notBlank;

public class SerialNumberFillerImpl implements SerialNumberFiller {
    private static final Logger log = LoggerFactory.getLogger(SerialNumberFillerImpl.class);
    private String defaultSerialNumberFieldName = "serialNumber";

    @Override
    public String getDefaultSerialNumberFieldName() {

        return defaultSerialNumberFieldName;
    }

    @Override
    public void setDefaultSerialNumberFieldName(String defaultSerialNumberFieldName) {

        this.defaultSerialNumberFieldName = notBlank(defaultSerialNumberFieldName);
    }

    @Override
    public <T> void fill(Collection<T> data, String fieldName, int pageNum, int pageSize) {
        if (CollUtil.isEmpty(data)) { return; }
        if (StrUtil.isBlank(fieldName)) { fieldName = "serialNumber"; }
        //
        int idx = 0;
        for (T datum : data) {
            if (datum == null) { continue; }
            int pageIndexBasedIndex = pageNum > 1 ? (pageNum - 1) * pageSize + idx + 1 : idx + 1;

            try {
                Field field = ReflectUtil.getField(datum.getClass(), fieldName);
                if (field == null) { continue; }
                ReflectUtil.setFieldValue(datum, field
                        , ConversionUtil.convert(pageIndexBasedIndex, field.getType()));
            } catch (Exception e) {
//                log.error("Fill serial number failure! ", e);
            }

            idx++;
        }
    }

}
