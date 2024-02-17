/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert;

import kunlun.util.Assert;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * The generic converter interface for converting between two or more types.
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
     * @author Kahle
     */
    final class ConvertiblePair {
        private final Class<?> sourceType;
        private final Class<?> targetType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            Assert.notNull(sourceType, "The source type must not be null. ");
            Assert.notNull(targetType, "The target type must not be null. ");
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
