package artoria.fake;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.*;

public class FakeUtils {
    private static Logger log = LoggerFactory.getLogger(FakeUtils.class);
    private static FakeProvider fakeProvider;

    public static FakeProvider getFakeProvider() {
        if (fakeProvider != null) { return fakeProvider; }
        synchronized (FakeUtils.class) {
            if (fakeProvider != null) { return fakeProvider; }
            FakeUtils.setFakeProvider(new SimpleFakeProvider());
            return fakeProvider;
        }
    }

    public static void setFakeProvider(FakeProvider fakeProvider) {
        Assert.notNull(fakeProvider, "Parameter \"fakeProvider\" must not null. ");
        log.info("Set fake provider: {}", fakeProvider.getClass().getName());
        FakeUtils.fakeProvider = fakeProvider;
    }

    public static String getDefaultFakerName() {

        return getFakeProvider().getDefaultFakerName();
    }

    public static void setDefaultFakerName(String defaultFakerName) {

        getFakeProvider().setDefaultFakerName(defaultFakerName);
    }

    public static void register(Faker faker) {

        getFakeProvider().register(faker);
    }

    public static void unregister(String fakerName) {

        getFakeProvider().unregister(fakerName);
    }

    public static <T> T fake(Class<T> clazz) {

        return getFakeProvider().fake(EMPTY_STRING, clazz);
    }

    public static <T> T fake(String expression, Class<T> clazz) {

        return getFakeProvider().fake(expression, clazz);
    }

    public static <T> T fake(Map<String, String> attrExpressMap, Class<T> clazz) {

        return getFakeProvider().fake(attrExpressMap, clazz);
    }

    public static <T> List<T> fakeList(String expression, Class<T> clazz) {

        return getFakeProvider().fakeList(expression, null, clazz);
    }

    public static <T> List<T> fakeList(Map<String, String> attrExpressMap, Class<T> clazz) {

        return getFakeProvider().fakeList(attrExpressMap, null, clazz);
    }

    public static <T> List<T> fakeList(String expression, Integer size, Class<T> clazz) {

        return getFakeProvider().fakeList(expression, size, clazz);
    }

    public static <T> List<T> fakeList(Map<String, String> attrExpressMap, Integer size, Class<T> clazz) {

        return getFakeProvider().fakeList(attrExpressMap, size, clazz);
    }

    public interface FakeProvider extends Faker {

        String getDefaultFakerName();

        void setDefaultFakerName(String defaultFakerName);

        void register(Faker faker);

        void unregister(String fakerName);

        <T> T fake(Map<String, String> attrExpressMap, Class<T> clazz);

        <T> List<T> fakeList(String expression, Integer size, Class<T> clazz);

        <T> List<T> fakeList(Map<String, String> attrExpressMap, Integer size, Class<T> clazz);

    }

    public static class SimpleFakeProvider extends SimpleFaker implements FakeProvider {
        private static final Map<String, Faker> FAKERS_MAP = new ConcurrentHashMap<String, Faker>();
        private String defaultFakerName;

        public SimpleFakeProvider() {
            setDefaultFakerName("simple");
            register(new SimpleFaker());
            register(new NameFaker());
            register(new TimeFaker());
            register(new IdentifierFaker());
        }

        @Override
        public String getDefaultFakerName() {

            return defaultFakerName;
        }

        @Override
        public void setDefaultFakerName(String defaultFakerName) {
            Assert.notBlank(defaultFakerName, "Parameter \"defaultFakerName\" must not blank. ");
            log.info("Set default faker name: {}", defaultFakerName);
            this.defaultFakerName = defaultFakerName;
        }

        @Override
        public void register(Faker faker) {
            Assert.notNull(faker, "Parameter \"faker\" must not null. ");
            String fakerName = faker.name();
            Assert.notBlank(fakerName, "Parameter \"fakerName\" must not blank. ");
            String fakerClassName = faker.getClass().getName();
            log.info("Register \"{}\" to \"{}\". ", fakerClassName, fakerName);
            FAKERS_MAP.put(fakerName.toLowerCase(), faker);
        }

        @Override
        public void unregister(String fakerName) {
            Assert.notBlank(fakerName, "Parameter \"fakerName\" must not blank. ");
            Faker remove = FAKERS_MAP.remove(fakerName.toLowerCase());
            if (remove != null) {
                String removeClassName = remove.getClass().getName();
                log.info("Unregister \"{}\" to \"{}\". ", removeClassName, fakerName);
            }
        }

        @Override
        public String name() {

            return getClass().getSimpleName();
        }

        @Override
        public <T> T fake(String expression, Class<T> clazz) {
            verifyParameters(expression, clazz);
            Class<?> wrapper = ClassUtils.getWrapper(clazz);
            boolean basicType = isBasicType(wrapper);
            boolean multiple = isMultiple(expression);
            if (basicType && multiple) {
                throw new IllegalArgumentException("Multiple expressions must be of a bean type. ");
            }
            if (basicType) {
                String fakerName = parseFakerName(expression);
                fakerName = fakerName.toLowerCase();
                Faker faker = FAKERS_MAP.get(fakerName);
                if (faker == null) {
                    // Auto judge here again?
                    // The judge logic will only get more and more complicated.
                    faker = FAKERS_MAP.get(defaultFakerName);
                }
                if (faker == null) {
                    throw new IllegalStateException("The default faker name corresponds to an empty faker. ");
                }
                return faker.fake(expression, clazz);
            }
            else {
                Map<String, String> attrExpressMap = null;
                if (multiple) {
                    attrExpressMap = parseMultiple(expression);
                }
                return fake(attrExpressMap, clazz);
            }
        }

        @Override
        public <T> List<T> fakeList(String expression, Integer size, Class<T> clazz) {
            verifyParameters(expression, clazz);
            if (size == null || size <= ZERO) {
                size = RandomUtils.nextInt(TEN) + ONE;
            }
            List<T> result = new ArrayList<T>();
            for (int i = ZERO; i < size; i++) {
                result.add(fake(expression, clazz));
            }
            return result;
        }

        @Override
        public <T> List<T> fakeList(Map<String, String> attrExpressMap, Integer size, Class<T> clazz) {
            Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
            if (size == null || size <= ZERO) {
                size = RandomUtils.nextInt(TEN) + ONE;
            }
            List<T> result = new ArrayList<T>();
            for (int i = ZERO; i < size; i++) {
                result.add(fake(attrExpressMap, clazz));
            }
            return result;
        }

    }

}
