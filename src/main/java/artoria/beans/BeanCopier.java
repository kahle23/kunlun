package artoria.beans;

import artoria.convert.ConversionProvider;

/**
 * The bean copier.
 * @author Kahle
 */
public interface BeanCopier {

    /**
     * Copy bean attributes to another bean.
     * @param from The bean will be copied
     * @param to The bean will be given
     * @param conversionProvider The type conversion provider and this value can be null
     */
    void copy(Object from, Object to, ConversionProvider conversionProvider);

}
