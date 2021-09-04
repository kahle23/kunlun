package artoria.beans;

import artoria.convert.ConversionProvider;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;
import artoria.util.ArrayUtils;
import artoria.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static artoria.common.Constants.ZERO;

/**
 * The bean copier simple implement by jdk.
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
    public void copy(Object from, Object to, ConversionProvider conversionProvider) {
        Assert.notNull(from, "Parameter \"from\" must is not null. ");
        Assert.notNull(to, "Parameter \"to\" must is not null. ");
        boolean haveCvn = conversionProvider != null;
        PropertyDescriptor[] fromDescriptors = ReflectUtils.getPropertyDescriptors(from.getClass());
        PropertyDescriptor[] toDescriptors = ReflectUtils.getPropertyDescriptors(to.getClass());
        Map<String, Method> fromMths = new HashMap<String, Method>(fromDescriptors.length);
        for (PropertyDescriptor fromDescriptor : fromDescriptors) {
            Method readMethod = fromDescriptor.getReadMethod();
            if (readMethod != null) { fromMths.put(fromDescriptor.getName(), readMethod); }
        }
        Map<String, Method> toMths = new HashMap<String, Method>(toDescriptors.length);
        for (PropertyDescriptor toDescriptor : toDescriptors) {
            Method writeMethod = toDescriptor.getWriteMethod();
            if (writeMethod != null) { toMths.put(toDescriptor.getName(), writeMethod); }
        }
        for (Map.Entry<String, Method> entry : fromMths.entrySet()) {
            String name = entry.getKey();
            Method destMth = toMths.get(name);
            if (destMth == null) { continue; }
            Method srcMth = entry.getValue();
            Class<?>[] types = destMth.getParameterTypes();
            try {
                boolean haveType = ArrayUtils.isNotEmpty(types);
                Object input = srcMth.invoke(from);
                if (input == null && haveType
                        && types[ZERO].isPrimitive()) {
                    throw new NullPointerException();
                }
                if (haveCvn && haveType) {
                    input = conversionProvider.convert(input, types[ZERO]);
                }
                destMth.invoke(to, input);
            }
            catch (Exception e) {
                if (ignoreException) {
                    log.debug("Execution \"copy\" error. ", e);
                }
                else {
                    throw ExceptionUtils.wrap(e);
                }
            }
        }
    }

}
