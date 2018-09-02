package artoria.beans;

import artoria.converter.TypeConverter;
import artoria.exception.ExceptionUtils;
import artoria.reflect.ReflectUtils;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static artoria.common.Constants.GET_OR_SET_LENGTH;
import static artoria.common.Constants.SET;

/**
 * Bean copier simple implement by jdk.
 * @author Kahle
 */
public class SimpleBeanCopier implements BeanCopier {
    private static Logger log = Logger.getLogger(SimpleBeanCopier.class.getName());
    private Boolean ignoreException = true;

    public Boolean getIgnoreException() {

        return ignoreException;
    }

    public void setIgnoreException(Boolean ignoreException) {

        this.ignoreException = ignoreException;
    }

    @Override
    public void copy(Object from, Object to, List<String> ignoreProperties, TypeConverter converter) {
        Assert.notNull(from, "Parameter \"from\" must is not null. ");
        Assert.notNull(to, "Parameter \"to\" must is not null. ");
        boolean hasIgnore = CollectionUtils.isNotEmpty(ignoreProperties);
        boolean hasCvt = converter != null;
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        Map<String, Method> fromMths = ReflectUtils.findReadMethods(fromClass);
        Map<String, Method> toMths = ReflectUtils.findWriteMethods(toClass);
        for (Map.Entry<String, Method> entry : fromMths.entrySet()) {
            String name = entry.getKey();
            name = name.substring(GET_OR_SET_LENGTH);
            // do ignore
            if (hasIgnore && ignoreProperties
                    .contains(StringUtils.uncapitalize(name))) {
                continue;
            }
            name = SET + name;
            Method destMth = toMths.get(name);
            if (destMth == null) { continue; }
            Method srcMth = entry.getValue();
            try {
                Object input = srcMth.invoke(from);
                Class<?> dType = destMth.getParameterTypes()[0];
                // do convert
                input = hasCvt ? converter.convert(input, dType) : input;
                destMth.invoke(to, input);
            }
            catch (Exception e) {
                if (this.ignoreException) {
                    log.fine(ExceptionUtils.toString(e));
                }
                else {
                    throw ExceptionUtils.wrap(e);
                }
            }
        }
    }

}
