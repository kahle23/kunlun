package com.github.kahlkn.artoria.beans;

import com.github.kahlkn.artoria.converter.Converter;
import com.github.kahlkn.artoria.util.Assert;

import java.util.List;

/**
 * Cglib bean copier.
 * @author Kahle
 */
public class CglibBeanCopier implements BeanCopier {

    private static class CglibConverterAdapter implements net.sf.cglib.core.Converter {
        private Converter converter;

        public Converter getConverter() {
            return converter;
        }

        public void setConverter(Converter converter) {
            Assert.notNull(converter, "Parameter \"converter\" must not null. ");
            this.converter = converter;
        }

        public CglibConverterAdapter(Converter converter) {
            this.setConverter(converter);
        }

        @Override
        public Object convert(Object o, Class aClass, Object o1) {
            return converter.convert(o, aClass);
        }

    }

    @Override
    public void copy(Object from, Object to, List<String> ignoreProperties, Converter converter) {
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        net.sf.cglib.beans.BeanCopier copier = net.sf.cglib.beans.BeanCopier.create(fromClass, toClass, true);
        CglibConverterAdapter adapter = new CglibConverterAdapter(converter);
        copier.copy(from, to, adapter);
    }

}
