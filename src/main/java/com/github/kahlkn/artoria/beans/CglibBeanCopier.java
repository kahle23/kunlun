package com.github.kahlkn.artoria.beans;

import com.github.kahlkn.artoria.converter.Converter;
import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.CollectionUtils;
import com.github.kahlkn.artoria.util.StringUtils;

import java.util.List;

import static com.github.kahlkn.artoria.util.Const.GET_OR_SET_LENGTH;

/**
 * Cglib bean copier.
 * @author Kahle
 */
public class CglibBeanCopier implements BeanCopier {

    private static class CglibConverterAdapter implements net.sf.cglib.core.Converter {
        private Converter converter;
        private List<String> ignoreProperties;
        private boolean hasIgnore;

        public void setConverter(Converter converter) {
            Assert.notNull(converter, "Parameter \"converter\" must not null. ");
            this.converter = converter;
        }

        public CglibConverterAdapter(Converter converter, List<String> ignoreProperties) {
            this.setConverter(converter);
            this.ignoreProperties = ignoreProperties;
            this.hasIgnore = CollectionUtils.isNotEmpty(ignoreProperties);
        }

        @Override
        public Object convert(Object value, Class valClass, Object methodName) {
            if (hasIgnore) {
                String tmp = (String) methodName;
                tmp = tmp.substring(GET_OR_SET_LENGTH);
                tmp = StringUtils.uncapitalize(tmp);
                if (ignoreProperties.contains(tmp)) {
                    return null;
                }
            }
            return converter.convert(value, valClass);
        }

    }

    @Override
    public void copy(Object from, Object to, List<String> ignoreProperties, Converter converter) {
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        net.sf.cglib.beans.BeanCopier copier = net.sf.cglib.beans.BeanCopier.create(fromClass, toClass, true);
        CglibConverterAdapter adapter = new CglibConverterAdapter(converter, ignoreProperties);
        copier.copy(from, to, adapter);
    }

}
