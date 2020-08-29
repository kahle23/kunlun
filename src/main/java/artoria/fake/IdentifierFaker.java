package artoria.fake;

import artoria.convert.type.TypeConvertUtils;
import artoria.identifier.IdentifierUtils;
import artoria.util.ClassUtils;
import artoria.util.ObjectUtils;

public class IdentifierFaker extends AbstractFaker {

    @Override
    public String name() {

        return "id";
    }

    @Override
    public <T> T fake(String expression, Class<T> clazz) {
        verifyParameters(expression, clazz);
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        Object object;
        if (String.class.isAssignableFrom(wrapper)) {
            object = IdentifierUtils.nextStringIdentifier();
        }
        else if (Number.class.isAssignableFrom(wrapper)) {
            object = IdentifierUtils.nextLongIdentifier();
        }
        else {
            return null;
        }
        Object convert = TypeConvertUtils.convert(object, wrapper);
        return ObjectUtils.cast(convert, clazz);
    }

}
