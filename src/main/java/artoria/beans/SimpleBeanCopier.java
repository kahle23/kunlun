package artoria.beans;

import artoria.converter.TypeConverter;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Bean copier simple implement by jdk.
 * @author Kahle
 */
public class SimpleBeanCopier implements BeanCopier {
    private static Logger log = LoggerFactory.getLogger(SimpleBeanCopier.class);
    private Boolean ignoreException = true;

    public Boolean getIgnoreException() {

        return ignoreException;
    }

    public void setIgnoreException(Boolean ignoreException) {
        Assert.notNull(ignoreException, "Parameter \"ignoreException\" must not null. ");
        this.ignoreException = ignoreException;
    }

    @Override
    public void copy(Object from, Object to, List<String> ignoreAttributes, TypeConverter converter) {
        Assert.notNull(from, "Parameter \"from\" must is not null. ");
        Assert.notNull(to, "Parameter \"to\" must is not null. ");
        boolean hasIgnore = CollectionUtils.isNotEmpty(ignoreAttributes);
        boolean hasCvt = converter != null;
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        Map<String, Method> fromMths = ReflectUtils.findReadMethods(fromClass);
        Map<String, Method> toMths = ReflectUtils.findWriteMethods(toClass);
        for (Map.Entry<String, Method> entry : fromMths.entrySet()) {
            String name = entry.getKey();
            // do ignore
            if (hasIgnore && ignoreAttributes.contains(name)) {
                continue;
            }
            Method destMth = toMths.get(name);
            if (destMth == null) { continue; }
            Method srcMth = entry.getValue();
            Class<?>[] types = destMth.getParameterTypes();
            try {
                boolean haveType = ArrayUtils.isNotEmpty(types);
                Object input = srcMth.invoke(from);
                if (input == null && haveType
                        && types[0].isPrimitive()) {
                    throw new NullPointerException();
                }
                if (hasCvt && haveType) {
                    input = converter.convert(input, types[0]);
                }
                destMth.invoke(to, input);
            }
            catch (Exception e) {
                if (this.ignoreException) {
                    log.debug("Execution \"copy\" error. ", e);
                }
                else {
                    throw ExceptionUtils.wrap(e);
                }
            }
        }
    }

}
