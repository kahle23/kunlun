package artoria.property;

import java.util.Map;

/**
 * The read only property source.
 * @author Kahle
 */
public abstract class BaseReadOnlyPropertySource extends AbstractPropertySource {

    public BaseReadOnlyPropertySource(String name) {

        super(name);
    }

    @Override
    public void setProperties(Map<?, ?> properties) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Object setProperty(String name, Object value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Object removeProperty(String name) {

        throw new UnsupportedOperationException();
    }

}
