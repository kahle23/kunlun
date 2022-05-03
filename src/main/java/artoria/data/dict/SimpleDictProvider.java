package artoria.data.dict;

import java.util.Collections;

/**
 * The simple data dictionary provider.
 * @author Kahle
 */
public class SimpleDictProvider extends AbstractDictProvider {

    public SimpleDictProvider() {

        super(Collections.<String, Object>emptyMap());
    }

    @Override
    public Dict getByName(String group, String name) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Dict getByCode(String group, String code) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Dict getByValue(String group, String value) {

        throw new UnsupportedOperationException();
    }

}
