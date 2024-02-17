/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert.support;

import kunlun.convert.ConditionalConverter;
import kunlun.convert.ConversionService;
import kunlun.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static kunlun.common.constant.Numbers.ZERO;

public class ListBasicToListBasicConverter extends AbstractTypeConverter implements ConditionalConverter {

    public ListBasicToListBasicConverter(ConversionService conversionService) {

        super(conversionService, List.class, List.class);
    }

    @Override
    public boolean matches(Type sourceType, Type targetType) {
        if (!(sourceType instanceof ParameterizedType)) { return false; }
        if (!(targetType instanceof ParameterizedType)) { return false; }
        if (((ParameterizedType) sourceType).getRawType() != List.class) { return false; }
        if (((ParameterizedType) targetType).getRawType() != List.class) { return false; }
        Type sourceArgsType = ((ParameterizedType) sourceType).getActualTypeArguments()[ZERO];
        Type targetArgsType = ((ParameterizedType) targetType).getActualTypeArguments()[ZERO];
        return getConversionService().canConvert(sourceArgsType, targetArgsType);
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
            Object convert = getConversionService().convert(sourceItem, sourceArgsType, targetArgsType);
            targetList.add(convert);
        }

        return targetList;
    }

}
