/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert.support;

public class ObjectToStringConverter extends AbstractClassConverter {

    public ObjectToStringConverter() {

        super(Object.class, String.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {

        return source.toString();
    }

}
