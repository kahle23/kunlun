package artoria.convert.type1.support;

import artoria.beans.BeanUtils;
import artoria.convert.type1.ConversionProvider;
import artoria.test.bean.Cat;
import artoria.test.bean.Dog;

public class CatToDogConverter extends AbstractClassConverter {

    public CatToDogConverter(ConversionProvider conversionProvider) {

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
