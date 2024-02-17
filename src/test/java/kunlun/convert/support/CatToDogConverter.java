/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert.support;

import kunlun.convert.ConversionService;
import kunlun.data.bean.BeanUtils;
import kunlun.test.pojo.entity.animal.Cat;
import kunlun.test.pojo.entity.animal.Dog;

public class CatToDogConverter extends AbstractClassConverter {

    public CatToDogConverter(ConversionService conversionProvider) {

        super(conversionProvider, Cat.class, Dog.class);
    }

    @Override
    public Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {
        Cat cat = (Cat) source;
        Dog dog = BeanUtils.beanToBean(cat, Dog.class);
        dog.setBreed("Cat: " + cat.getBreed());
        dog.setSize("Cat");
        dog.setSound("Miaow");
        return dog;
    }

}
