package com.apyhs.artoria.beans;

import com.apyhs.artoria.converter.Converter;
import com.apyhs.artoria.exception.UncheckedException;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.CollectionUtils;
import com.apyhs.artoria.reflect.ReflectUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.apyhs.artoria.util.StringConstant.STRING_GET;
import static com.apyhs.artoria.util.StringConstant.STRING_SET;

/**
 * Jdk bean copier.
 * @author Kahle
 */
public class JdkBeanCopier implements BeanCopier {
    private static final Integer GET_OR_SET_LENGTH = 3;

    private Boolean ignoreException = true;

    public Boolean getIgnoreException() {
        return ignoreException;
    }

    public void setIgnoreException(Boolean ignoreException) {
        this.ignoreException = ignoreException;
    }

    @Override
    public void copy(Object from, Object to, List<String> ignoreProperties, Converter converter) {
        Assert.notNull(from, "Parameter \"from\" must is not null. ");
        Assert.notNull(to, "Parameter \"to\" must is not null. ");
        boolean hasIgnore = CollectionUtils.isNotEmpty(ignoreProperties);
        boolean hasCvt = converter != null;
        Class<?> srcClass = from.getClass();
        Class<?> destClass = to.getClass();
        Map<String, Method> srcMths = ReflectUtils.findAllGetterAndSetter(srcClass);
        Map<String, Method> destMths = ReflectUtils.findAllGetterAndSetter(destClass);
        for (Map.Entry<String, Method> entry : srcMths.entrySet()) {
            String name = entry.getKey();
            if (!name.startsWith(STRING_GET)) { continue; }
            name = name.substring(GET_OR_SET_LENGTH);
            // do ignore
            if (hasIgnore && ignoreProperties.contains(name)) {
                continue;
            }
            name = STRING_SET + name;
            Method destMth = destMths.get(name);
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
                if (ignoreException) { continue; }
                throw new UncheckedException(e.getMessage(), e);
            }
        }

    }

}
