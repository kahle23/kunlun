/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert.support;

public class StringToBooleanConverter extends AbstractClassConverter {

    public StringToBooleanConverter() {

        super(String.class, Boolean.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {

        return Boolean.valueOf((String) source);
    }

}
