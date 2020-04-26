package artoria.util;

import java.io.Serializable;
import java.lang.reflect.*;

import static artoria.common.Constants.ONE;
import static artoria.common.Constants.ZERO;

/**
 * Type tools.
 * @author Kahle
 */
public class TypeUtils {
    private static final Type[] EMPTY_TYPE_ARRAY = new Type[]{};

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            Class<?> componentClass = clazz.getComponentType();
            Type componentType = TypeUtils.canonicalize(componentClass);
            return clazz.isArray() ? new GenericArrayTypeImpl(componentType) : clazz;
        }
        else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Type ownerType = parameterizedType.getOwnerType();
            Type rawType = parameterizedType.getRawType();
            return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
        }
        else if (type instanceof GenericArrayType) {
            GenericArrayType arrayType = (GenericArrayType) type;
            Type componentType = arrayType.getGenericComponentType();
            return new GenericArrayTypeImpl(componentType);
        }
        else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            Type[] lowerBounds = wildcardType.getLowerBounds();
            return new WildcardTypeImpl(upperBounds, lowerBounds);
        }
        else {
            // Type is either serializable as-is or unsupported.
            return type;
        }
    }

    public static void notPrimitive(Type type, String message) {
        boolean notPrimitive = !(type instanceof Class<?>) || !((Class<?>) type).isPrimitive();
        if (!notPrimitive) {
            throw new IllegalStateException(message);
        }
    }

    public static WildcardType subtypeOf(Type bound) {
        Assert.notNull(bound, "Parameter \"bound\" must not null. ");
        Type[] upperBounds;
        if (bound instanceof WildcardType) {
            upperBounds = ((WildcardType) bound).getUpperBounds();
        }
        else {
            upperBounds = new Type[] { bound };
        }
        return new WildcardTypeImpl(upperBounds, EMPTY_TYPE_ARRAY);
    }

    public static WildcardType supertypeOf(Type bound) {
        Assert.notNull(bound, "Parameter \"bound\" must not null. ");
        Type[] lowerBounds;
        if (bound instanceof WildcardType) {
            lowerBounds = ((WildcardType) bound).getLowerBounds();
        }
        else {
            lowerBounds = new Type[] { bound };
        }
        return new WildcardTypeImpl(new Type[] { Object.class }, lowerBounds);
    }

    public static GenericArrayType arrayOf(Type componentType) {
        Assert.notNull(componentType, "Parameter \"componentType\" must not null. ");
        return new GenericArrayTypeImpl(componentType);
    }

    public static ParameterizedType parameterizedOf(Type rawType, Type... typeArguments) {
        Assert.notNull(rawType, "Parameter \"rawType\" must not null. ");
        return new ParameterizedTypeImpl(null, rawType, typeArguments);
    }

    public static ParameterizedType parameterizedWithOwnerOf(Type ownerType, Type rawType, Type... typeArguments) {
        Assert.notNull(rawType, "Parameter \"rawType\" must not null. ");
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }

    static class WildcardTypeImpl implements WildcardType, Serializable {
        private final Type upperBound;
        private final Type lowerBound;

        WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            Assert.isTrue(
                    upperBounds.length == ONE
                    , "Parameter \"upperBounds\" length must == 1. "
            );
            Assert.isTrue(
                    lowerBounds.length <= ONE
                    , "Parameter \"lowerBounds\" length must <= 1. "
            );
            if (lowerBounds.length == ONE) {
                Assert.notNull(
                        lowerBounds[ZERO]
                        , "LowerBounds first element must not null. "
                );
                TypeUtils.notPrimitive(
                        lowerBounds[ZERO]
                        , "LowerBounds first element must not primitive. "
                );
                Assert.isTrue(
                        upperBounds[ZERO] == Object.class
                        , "UpperBounds first element must be \"Object.class\". "
                );
                this.lowerBound = TypeUtils.canonicalize(lowerBounds[ZERO]);
                this.upperBound = Object.class;
            }
            else {
                Assert.notNull(
                        upperBounds[ZERO]
                        , "UpperBounds first element must not null. "
                );
                TypeUtils.notPrimitive(
                        upperBounds[ZERO]
                        , "UpperBounds first element must not primitive. "
                );
                this.lowerBound = null;
                this.upperBound = TypeUtils.canonicalize(upperBounds[ZERO]);
            }
        }

        @Override
        public Type[] getUpperBounds() {

            return new Type[] { upperBound };
        }

        @Override
        public Type[] getLowerBounds() {

            return lowerBound != null ? new Type[] { lowerBound } : EMPTY_TYPE_ARRAY;
        }

    }

    static class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private final Type componentType;

        GenericArrayTypeImpl(Type componentType) {
            Assert.notNull(
                    componentType
                    , "Parameter \"componentType\" must not null. "
            );
            this.componentType = TypeUtils.canonicalize(componentType);
        }

        @Override
        public Type getGenericComponentType() {

            return componentType;
        }

    }

    static class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
            if (rawType instanceof Class<?>) {
                // Require an owner type if the raw type needs it.
                Class<?> rawTypeAsClass = (Class<?>) rawType;
                boolean isStaticOrTopLevelClass =
                        Modifier.isStatic(rawTypeAsClass.getModifiers())
                                || rawTypeAsClass.getEnclosingClass() == null;
                Assert.isTrue(
                        ownerType != null || isStaticOrTopLevelClass
                        , "Parameter \"ownerType\" must not null or " +
                                "parameter \"rawType\" must be static or top level class " +
                                "when parameter \"rawType\" is instance of \"Class\". "
                );
            }
            this.ownerType = ownerType != null ? TypeUtils.canonicalize(ownerType) : null;
            this.rawType = TypeUtils.canonicalize(rawType);
            this.typeArguments = typeArguments.clone();
            for (int i = ZERO; i < this.typeArguments.length; i++) {
                Type typeArgument = this.typeArguments[i];
                Assert.notNull(typeArgument, "TypeArguments each element must not null. ");
                TypeUtils.notPrimitive(typeArgument, "TypeArguments each element must not primitive. ");
                this.typeArguments[i] = TypeUtils.canonicalize(typeArgument);
            }
        }

        @Override
        public Type[] getActualTypeArguments() {

            return typeArguments.clone();
        }

        @Override
        public Type getRawType() {

            return rawType;
        }

        @Override
        public Type getOwnerType() {

            return ownerType;
        }

    }

}
