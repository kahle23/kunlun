package artoria.beans;

import artoria.converter.TypeConverter;

import java.util.List;

/**
 * The bean copier.
 * @author Kahle
 */
public interface BeanCopier {

    /**
     * Copy bean attributes to another bean.
     * @param from The bean will be copied
     * @param to The bean will be given
     * @param ignoreAttributes The ignore attributes
     *                         from the bean who will be given
     *                         and this value can be null
     * @param converter The attribute converter
     *                  and this value can be null
     */
    void copy(Object from, Object to, List<String> ignoreAttributes, TypeConverter converter);

}
