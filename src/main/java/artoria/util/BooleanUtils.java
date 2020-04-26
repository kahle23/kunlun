package artoria.util;

import static artoria.common.Constants.*;

/**
 * Boolean tools.
 * @author Kahle
 */
public class BooleanUtils {

    public static boolean parseBoolean(Object boolObject) {
        Boolean booleanObj = BooleanUtils.valueOf(boolObject);
        return Boolean.TRUE.equals(booleanObj);
    }

    public static Boolean valueOf(Object boolObject) {
        if (boolObject == null) { return null; }
        if (boolObject instanceof Boolean) {
            return (Boolean) boolObject;
        }
        String boolStr = String.valueOf(boolObject);
        return BooleanUtils.valueOf(boolStr);
    }

    public static Boolean valueOf(String boolString) {
        if (boolString == null) { return null; }
        switch (boolString.length()) {
            case ONE: {
                return "y".equalsIgnoreCase(boolString)
                        || "t".equalsIgnoreCase(boolString)
                        || "1".equals(boolString)
                        ? Boolean.TRUE :
                        "n".equalsIgnoreCase(boolString)
                        || "f".equalsIgnoreCase(boolString)
                        || "0".equals(boolString)
                        ? Boolean.FALSE : null;
            }
            case TWO: {
                return ON.equalsIgnoreCase(boolString) ? Boolean.TRUE :
                        NO.equalsIgnoreCase(boolString) ? Boolean.FALSE : null;
            }
            case THREE: {
                return YES.equalsIgnoreCase(boolString) ? Boolean.TRUE :
                        OFF.equalsIgnoreCase(boolString) ? Boolean.FALSE : null;
            }
            case FOUR: {
                return TRUE.equalsIgnoreCase(boolString) ? Boolean.TRUE : null;
            }
            case FIVE: {
                return FALSE.equalsIgnoreCase(boolString) ? Boolean.FALSE : null;
            }
            default: { return null; }
        }
    }

}
