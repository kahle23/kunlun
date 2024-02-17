/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.bean.support;

import kunlun.convert.ConversionService;
import kunlun.data.bean.BeanCopier;
import kunlun.exception.ExceptionUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.reflect.ReflectUtils;
import kunlun.util.ArrayUtils;
import kunlun.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static kunlun.common.constant.Numbers.ZERO;

/**
 * The bean copier simple implement by jdk.
 * @author Kahle
 */
public class SimpleBeanCopier implements BeanCopier {
    private static final Logger log = LoggerFactory.getLogger(SimpleBeanCopier.class);
    private Boolean ignoreException = true;

    public Boolean getIgnoreException() {

        return ignoreException;
    }

    public void setIgnoreException(Boolean ignoreException) {
        Assert.notNull(ignoreException, "Parameter \"ignoreException\" must not null. ");
        this.ignoreException = ignoreException;
    }

    @Override
    public void copy(Object from, Object to, ConversionService conversionService) {
        Assert.notNull(from, "Parameter \"from\" must is not null. ");
        Assert.notNull(to, "Parameter \"to\" must is not null. ");
        boolean haveCvn = conversionService != null;
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
                    input = conversionService.convert(input, types[ZERO]);
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
