package artoria.convert.type1;

import artoria.util.Assert;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Generic converter interface for converting between two or more types.
 * @author Kahle
 */
public interface GenericConverter {

    /**
     * Return the source and target types that this converter can convert between.
     * @return The convertible source-to-target type pair
     */
    Set<ConvertiblePair> getConvertibleTypes();

    /**
     * Convert the source object to the target type.
     * @param source The source object to convert
     * @param sourceType The source type to convert from
     * @param targetType The target type to convert to
     * @return The converted object or source object
     */
    Object convert(Object source, Type sourceType, Type targetType);

    /**
     * The source-to-target pair.
     */
    final class ConvertiblePair {
        private final Class<?> sourceType;
        private final Class<?> targetType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            Assert.notNull(sourceType, "Source type must not be null. ");
            Assert.notNull(targetType, "Target type must not be null. ");
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public Class<?> getSourceType() {

            return sourceType;
        }

        public Class<?> getTargetType() {

            return targetType;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) { return true; }
            if (object == null || getClass() != object.getClass()) { return false; }
            ConvertiblePair that = (ConvertiblePair) object;
            return sourceType == that.sourceType && targetType == that.targetType;
        }

        @Override
        public int hashCode() {

            return sourceType.hashCode() * 31 + targetType.hashCode();
        }

        @Override
        public String toString() {

            return sourceType.getName() + " -> " + targetType.getName();
        }

    }

}
