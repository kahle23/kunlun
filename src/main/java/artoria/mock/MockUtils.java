package artoria.mock;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

public class MockUtils {
    private static final Logger log = LoggerFactory.getLogger(MockUtils.class);
    private static volatile MockProvider mockProvider;

    public static MockProvider getMockProvider() {
        if (mockProvider != null) { return mockProvider; }
        synchronized (MockUtils.class) {
            if (mockProvider != null) { return mockProvider; }
            setMockProvider(new SimpleMockProvider());
            return mockProvider;
        }
    }

    public static void setMockProvider(MockProvider mockProvider) {
        Assert.notNull(mockProvider, "Parameter \"mockProvider\" must not null. ");
        log.info("Set mock provider: {}", mockProvider.getClass().getName());
        MockUtils.mockProvider = mockProvider;
    }

    public static <T> T mock(Class<T> clazz, MockFeature... features) {

        return getMockProvider().mock(clazz, features);
    }

    public static <T> T mock(Type type, MockFeature... features) {

        return getMockProvider().mock(type, features);
    }

}
