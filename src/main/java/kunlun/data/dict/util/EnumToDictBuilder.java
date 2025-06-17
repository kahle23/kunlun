/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict.util;

import kunlun.common.constant.Nil;
import kunlun.core.Builder;
import kunlun.data.CodeDefinition;
import kunlun.data.dict.DataDict;
import kunlun.data.dict.model.DataDictImpl;
import kunlun.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.EMPTY_STRING;
import static kunlun.util.Assert.isAssignable;
import static kunlun.util.Assert.notBlank;
import static kunlun.util.StrUtil.camelToUnderline;
import static kunlun.util.StrUtil.uncapitalize;

/**
 * The enum to dict dictionary.
 * @author Kahle
 */
public class EnumToDictBuilder<E extends Enum<E>> implements Builder {
    protected final Class<E> enumClass;
    protected final String groupCode;

    public EnumToDictBuilder(String groupCode, Class<E> enumClass) {
        isAssignable(CodeDefinition.class, enumClass);
        if (StrUtil.isBlank(groupCode)) {
            String smpName = uncapitalize(enumClass.getSimpleName());
            groupCode = camelToUnderline(smpName).toLowerCase();
        }
        this.groupCode = notBlank(groupCode);
        this.enumClass = enumClass;
    }

    public EnumToDictBuilder(Class<E> enumClass) {

        this(Nil.STR, enumClass);
    }

    protected DataDict processEnumConstant(E eEnum, int index) {
        CodeDefinition definition = (CodeDefinition) eEnum;
        // Build data dictionary.
        DataDictImpl dataDict = new DataDictImpl();
        dataDict.setGroupCode(groupCode);
        dataDict.setName(definition.getDescription());
        dataDict.setCode(eEnum.name());
        dataDict.setValue(definition.getCode() != null ? String.valueOf(definition.getCode()) : EMPTY_STRING);
        //dataDict.setDescription();
        dataDict.setSort(index);
        return dataDict;
    }

    @Override
    public List<DataDict> build() {
        List<DataDict> result = new ArrayList<DataDict>();
        int index = ZERO;
        for (E eEnum : enumClass.getEnumConstants()) {
            result.add(processEnumConstant(eEnum, index));
            index++;
        }
        return result;
    }

}
