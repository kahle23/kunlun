package artoria.convert.type1.support;

import artoria.convert.type1.ConditionalConverter;
import artoria.convert.type1.ConversionProvider;
import artoria.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static artoria.common.Constants.ZERO;

public class ListBasicToListBasicConverter extends AbstractTypeConverter implements ConditionalConverter {

    public ListBasicToListBasicConverter(ConversionProvider conversionProvider) {

        super(conversionProvider, List.class, List.class);
    }

    @Override
    public boolean matches(Type sourceType, Type targetType) {
        if (!(sourceType instanceof ParameterizedType)) { return false; }
        if (!(targetType instanceof ParameterizedType)) { return false; }
        if (((ParameterizedType) sourceType).getRawType() != List.class) { return false; }
        if (((ParameterizedType) targetType).getRawType() != List.class) { return false; }
        Type sourceArgsType = ((ParameterizedType) sourceType).getActualTypeArguments()[ZERO];
        Type targetArgsType = ((ParameterizedType) targetType).getActualTypeArguments()[ZERO];
        return getConversionProvider().canConvert(sourceArgsType, targetArgsType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Object source, Type sourceType, Type targetType) {
        Assert.notNull(sourceType, "Parameter \"sourceType\" must not null. ");
        Assert.notNull(targetType, "Parameter \"targetType\" must not null. ");
        Assert.notNull(source, "Parameter \"source\" must not null. ");

        List targetList = new ArrayList();
        List sourceList = (List) source;
        
        Type sourceArgsType = ((ParameterizedType) sourceType).getActualTypeArguments()[ZERO];
        Type targetArgsType = ((ParameterizedType) targetType).getActualTypeArguments()[ZERO];

        for (Object sourceItem : sourceList) {
            Object convert = getConversionProvider().convert(sourceItem, sourceArgsType, targetArgsType);
            targetList.add(convert);
        }

        return targetList;
    }

}
