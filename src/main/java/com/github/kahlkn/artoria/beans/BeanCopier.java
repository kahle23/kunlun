package com.github.kahlkn.artoria.beans;

import com.github.kahlkn.artoria.converter.Converter;

import java.util.List;

/**
 * The bean copier.
 * @author Kahle
 */
public interface BeanCopier {

    /**
     * Copy bean properties to another bean.
     * @param from The bean will be copied
     * @param to The bean will be given
     * @param ignoreProperties The ignore properties
     *                         from the bean who will be given
     *                         and this value can be null
     * @param converter The property converter
     *                  and this value can be null
     */
    void copy(Object from, Object to, List<String> ignoreProperties, Converter converter);

}
