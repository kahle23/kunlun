package artoria.generator.id;

import artoria.generator.id.support.SimpleIdGenerator;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple identifier provider.
 * @author Kahle
 */
public class SimpleIdProvider implements IdProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleIdProvider.class);
    protected final Map<String, IdGenerator> idGenerators;
    protected final Map<String, Object> commonProperties;

    protected SimpleIdProvider(Map<String, Object> commonProperties,
                               Map<String, IdGenerator> idGenerators) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(idGenerators, "Parameter \"idGenerators\" must not null. ");
        this.commonProperties = commonProperties;
        this.idGenerators = idGenerators;
    }

    public SimpleIdProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, IdGenerator>());
        registerGenerator("uuid", new SimpleIdGenerator());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerGenerator(String name, IdGenerator idGenerator) {
        Assert.notNull(idGenerator, "Parameter \"idGenerator\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = idGenerator.getClass().getName();
        idGenerator.setProperties(getCommonProperties());
        idGenerators.put(name, idGenerator);
        log.info("Register the id generator \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterGenerator(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        IdGenerator remove = idGenerators.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the id generator \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public IdGenerator getIdGenerator(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return idGenerators.get(name);
    }

    @Override
    public Object next(String name, Object... arguments) {
        IdGenerator generator = getIdGenerator(name);
        Assert.notNull(generator
                , "The corresponding id generator could not be found by name. ");
        return generator.next(arguments);
    }

}
