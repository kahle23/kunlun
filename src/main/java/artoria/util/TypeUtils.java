package artoria.util;

import java.io.Serializable;
import java.lang.reflect.*;

/**
 * Type tools.
 * @author Kahle
 */
public class TypeUtils {
    private static final Type[] EMPTY_TYPE_ARRAY = new Type[] {};

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

    public static class WildcardTypeImpl implements WildcardType, Serializable {
        private final Type upperBound;
        private final Type lowerBound;

        public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            Assert.state(upperBounds.length == 1
                    , "Parameter \"upperBounds\" length must == 1. ");
            Assert.state(lowerBounds.length <= 1
                    , "Parameter \"lowerBounds\" length must <= 1. ");
            if (lowerBounds.length == 1) {
                Assert.notNull(lowerBounds[0]
                        , "LowerBounds first element must not null. ");
                TypeUtils.notPrimitive(lowerBounds[0]
                        , "LowerBounds first element must not primitive. ");
                Assert.state(upperBounds[0] == Object.class
                        , "UpperBounds first element must be \"Object.class\". ");
                this.lowerBound = TypeUtils.canonicalize(lowerBounds[0]);
                this.upperBound = Object.class;
            }
            else {
                Assert.notNull(upperBounds[0]
                        , "UpperBounds first element must not null. ");
                TypeUtils.notPrimitive(upperBounds[0]
                        , "UpperBounds first element must not primitive. ");
                this.lowerBound = null;
                this.upperBound = TypeUtils.canonicalize(upperBounds[0]);
            }
        }

        public Type[] getUpperBounds() {

            return new Type[] { upperBound };
        }

        public Type[] getLowerBounds() {

            return lowerBound != null ? new Type[] { lowerBound } : EMPTY_TYPE_ARRAY;
        }

    }

    public static class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private final Type componentType;

        public GenericArrayTypeImpl(Type componentType) {
            Assert.notNull(componentType, "Parameter \"componentType\" must not null. ");
            this.componentType = TypeUtils.canonicalize(componentType);
        }

        @Override
        public Type getGenericComponentType() {

            return componentType;
        }

    }

    public static class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
            if (rawType instanceof Class<?>) {
                // Require an owner type if the raw type needs it.
                Class<?> rawTypeAsClass = (Class<?>) rawType;
                boolean isStaticOrTopLevelClass =
                        Modifier.isStatic(rawTypeAsClass.getModifiers())
                        || rawTypeAsClass.getEnclosingClass() == null;
                Assert.state(ownerType != null || isStaticOrTopLevelClass
                        , "Parameter \"ownerType\" must not null " +
                                "or parameter \"rawType\" must be static or top level class " +
                                "when parameter \"rawType\" is instance of \"Class\". ");
            }
            this.ownerType = ownerType == null ? null : TypeUtils.canonicalize(ownerType);
            this.rawType = TypeUtils.canonicalize(rawType);
            this.typeArguments = typeArguments.clone();
            for (int i = 0; i < this.typeArguments.length; i++) {
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
