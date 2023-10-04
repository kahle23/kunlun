package artoria.data;

import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * An arraylist that doesn't have to worry about generics.
 * @author Kahle
 */
public class Array extends ArrayList<Object> {

    public static Array of(Collection<?> collection) {

        return of().pushAll(collection);
    }

    public static Array of(Object[] objects) {

        return of().pushAll(objects);
    }

    public static Array of() {

        return new Array();
    }

    public Array push(Object object) {
        if (object != null) {
            Assert.isTrue(add(object)
                    , "Append the specified element failure. ");
        }
        return this;
    }

    public Array pushAll(Collection<?> collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            Assert.isTrue(addAll(collection)
                    , "Append all the specified elements failure. ");
        }
        return this;
    }

    public Array pushAll(Object[] objects) {
        if (ArrayUtils.isNotEmpty(objects)) {
            Assert.isTrue(addAll(Arrays.asList(objects))
                    , "Append all the specified elements failure. ");
        }
        return this;
    }

}
