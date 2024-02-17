/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.bean;

import kunlun.convert.ConversionService;

/**
 * The bean copier.
 * @author Kahle
 */
public interface BeanCopier {

    /**
     * Copy bean attributes to another bean.
     * @param from The bean will be copied
     * @param to The bean will be given
     * @param conversionService The type conversion service and this value can be null
     */
    void copy(Object from, Object to, ConversionService conversionService);

}
