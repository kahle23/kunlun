package com.github.kahlkn.artoria.beans;

import com.github.kahlkn.artoria.converter.Converter;
import com.github.kahlkn.artoria.exception.UncheckedException;
import com.github.kahlkn.artoria.reflect.ReflectUtils;
import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.CollectionUtils;
import com.github.kahlkn.artoria.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.github.kahlkn.artoria.util.Const.GET_OR_SET_LENGTH;
import static com.github.kahlkn.artoria.util.Const.SET;

/**
 * Jdk bean copier.
 * @author Kahle
 */
public class JdkBeanCopier implements BeanCopier {

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
                if (ignoreException) { continue; }
                throw new UncheckedException(e.getMessage(), e);
            }
        }

    }

}
