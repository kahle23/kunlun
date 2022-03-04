package artoria.action;

import artoria.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract action handler.
 * @author Kahle
 */
public abstract class AbstractActionHandler implements ActionHandler {
    private Map<Object, Object> attrs = Collections.emptyMap();

    protected void isSupport(Class<?>[] supportClasses, Class<?> clazz) {
        if (Object.class.equals(clazz)) { return; }
        for (Class<?> supportClass : supportClasses) {
            if (supportClass.equals(clazz)) { return; }
        }
        throw new IllegalArgumentException("Parameter \"clazz\" is not supported. ");
    }

    @Override
    public void attrs(Map<?, ?> attrs) {
        Assert.notNull(attrs, "Parameter \"attrs\" must not null. ");
        this.attrs = Collections.unmodifiableMap(attrs);
    }

    @Override
    public Map<Object, Object> attrs() {

        return attrs;
    }

}
