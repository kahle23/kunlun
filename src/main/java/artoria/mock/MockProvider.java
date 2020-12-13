package artoria.mock;

import java.lang.reflect.Type;

public interface MockProvider {

    <T> T mock(Type type, MockFeature... features);

}
