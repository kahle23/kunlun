package artoria.data.bean;

import artoria.convert.ConversionService;

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
