package artoria.cache;

import artoria.collection.ReferenceMap;

import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.ZERO;

/**
 * Memory cache simple implement by jdk.
 * @author Kahle
 */
public class SimpleCache extends AbstractCache {

    public SimpleCache(String name) {

        this(name, ZERO, ReferenceMap.Type.WEAK);
    }

    public SimpleCache(String name, long capacity) {

        this(name, capacity, ReferenceMap.Type.WEAK);
    }

    public SimpleCache(String name, ReferenceMap.Type type) {

        this(name, ZERO, type);
    }

    public SimpleCache(String name, long capacity, ReferenceMap.Type type) {
        super(
                name, capacity, new ReferenceMap<Object, ValueWrapper>(
                        new ConcurrentHashMap<Object, ReferenceMap.ValueCell<Object, ValueWrapper>>(),
                        type
                )
        );
    }

}
