package artoria.convert.support;

import artoria.data.bean.BeanUtils;
import artoria.convert.ConversionProvider;
import artoria.test.pojo.entity.animal.Cat;
import artoria.test.pojo.entity.animal.Dog;

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
