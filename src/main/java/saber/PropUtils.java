package saber;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PropUtils {
    private static final ConcurrentHashMap<String, Prop> CACHE = new ConcurrentHashMap<>();

    public static Prop use(String fileName) {
        Prop result = CACHE.get(fileName);
        if (result == null) {
            result = new Prop(fileName);
            CACHE.put(fileName, result);
        }
        return result;
    }

    public static Prop use(String fileName, String encoding) {
        Prop result = CACHE.get(fileName);
        if (result == null) {
            result = new Prop(fileName, encoding);
            CACHE.put(fileName, result);
        }
        return result;
    }

    public static Prop remove(String fileName) {
        return CACHE.remove(fileName);
    }

    public static void clear() {
        CACHE.clear();
    }

    public static Prop load(String fileName) {
        return new Prop(fileName);
    }

    public static Prop load(String fileName, String encoding) {
        return new Prop(fileName, encoding);
    }

    public static Prop load(File file) {
        return new Prop(file);
    }

    public static Prop load(File file, String encoding) {
        return new Prop(file, encoding);
    }

    public static Prop load(InputStream in) {
        return new Prop(in);
    }

    public static Prop load(InputStream in, String encoding) {
        return new Prop(in, encoding);
    }

    public static Prop load(Reader reader) {
        return new Prop(reader);
    }

}
