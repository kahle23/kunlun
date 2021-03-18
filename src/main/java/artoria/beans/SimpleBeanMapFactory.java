package artoria.beans;

import static artoria.convert.type.TypeConvertUtils.getConvertProvider;

public class SimpleBeanMapFactory implements BeanMapFactory {

    @Override
    public BeanMap getInstance() {
        SimpleBeanMap beanMap = new SimpleBeanMap();
        beanMap.setTypeConverter(getConvertProvider());
        return beanMap;
    }

    @Override
    public BeanMap getInstance(Object bean) {
        BeanMap beanMap = getInstance();
        beanMap.setBean(bean);
        return beanMap;
    }

}
