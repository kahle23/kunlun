package artoria.beans;

import artoria.convert.TypeConverter;

/**
 * The bean copier.
 * @author Kahle
 */
public interface BeanCopier {

    /**
     * Copy bean attributes to another bean.
     * @param from The bean will be copied
     * @param to The bean will be given
     * @param converter The attribute converter
     *                  and this value can be null
     */
    void copy(Object from, Object to, TypeConverter converter);

}
