package artoria.data;

/**
 * Can convert to or convert back from bean.
 * @author Kahle
 */
public interface Beanable {

    /**
     * Convert to bean.
     * @param clazz Bean class
     * @param <T> Bean type
     * @return Converted bean
     */
    <T> T toBean(Class<T> clazz);

    /**
     * Convert back from bean.
     * @param bean Input bean
     * @param <T> Bean type
     */
    <T> void fromBean(T bean);

}
