package artoria.fake;

import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.StringUtils;

import java.util.*;

import static artoria.common.Constants.*;

public abstract class AbstractFaker implements Faker {

    private void fillMapWithArray(Map<String, String> map, String[] array, String split) {
        if (ArrayUtils.isEmpty(array)) { return; }
        for (String str : array) {
            if (StringUtils.isBlank(str)) { continue; }
            String[] kvArray = str.split(split);
            String key = kvArray[ZERO];
            if (StringUtils.isBlank(key)) { continue; }
            String val = kvArray.length >= TWO ? kvArray[ONE] : EMPTY_STRING;
            map.put(key.trim(), val.trim());
        }
    }

    protected boolean isBasicType(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        return Number.class.isAssignableFrom(wrapper) ||
                Boolean.class.isAssignableFrom(wrapper) ||
                Character.class.isAssignableFrom(wrapper) ||
                Date.class.isAssignableFrom(wrapper) ||
                String.class.isAssignableFrom(wrapper);
    }

    protected boolean isMultiple(String expression) {

        return expression != null && expression.contains(VERTICAL_BAR);
    }

    protected Map<String, String> parseMultiple(String expression) {
        Assert.notNull(expression, "Parameter \"expression\" must not null. ");
        String[] strArray = expression.split("\\|");
        Map<String, String> result = new HashMap<String, String>(strArray.length);
        fillMapWithArray(result, strArray, EQUAL);
        return result;
    }

    protected String parseFakerName(String expression) {
        Assert.notNull(expression, "Parameter \"expression\" must not null. ");
        int indexOf = expression.indexOf(DOT);
        boolean exist = indexOf != MINUS_ONE;
        return exist ? expression.substring(ZERO, indexOf) : expression;
    }

    protected String parseDirective(String expression) {
        Assert.notNull(expression, "Parameter \"expression\" must not null. ");
        int bracketIndex = expression.indexOf(LEFT_SQUARE_BRACKET);
        int dotIndex = expression.indexOf(DOT);
        if (dotIndex == MINUS_ONE) { return EMPTY_STRING; }
        int beginIndex = dotIndex + ONE;
        int endIndex = expression.length();
        if (beginIndex >= endIndex) { return EMPTY_STRING; }
        if (bracketIndex != MINUS_ONE) { endIndex = bracketIndex; }
        return expression.substring(beginIndex, endIndex);
    }

    protected Map<String, String> parseParameters(String expression) {
        Assert.notNull(expression, "Parameter \"expression\" must not null. ");
        int rightIndex = expression.indexOf(RIGHT_SQUARE_BRACKET);
        int leftIndex = expression.indexOf(LEFT_SQUARE_BRACKET);
        if (leftIndex == MINUS_ONE) { return Collections.emptyMap(); }
        if (rightIndex == MINUS_ONE) { return Collections.emptyMap(); }
        if (rightIndex <= leftIndex) { return Collections.emptyMap(); }
        String paramsStr = expression.substring(leftIndex + ONE, rightIndex);
        if (StringUtils.isBlank(paramsStr)) { return Collections.emptyMap(); }
        String[] strArray = paramsStr.split(COMMA);
        Map<String, String> result = new HashMap<String, String>(strArray.length);
        fillMapWithArray(result, strArray, COLON);
        return result;
    }

    protected void verifyUnsupportedClass(Class<?> clazz) {
        boolean isUnsupported = clazz.isArray()
                || Collection.class.isAssignableFrom(clazz)
                || Map.class.isAssignableFrom(clazz);
        if (isUnsupported) {
            throw new UnsupportedOperationException("Collection, map, array are not supported. ");
        }
    }

    protected void verifyParameters(String expression, Class<?> clazz) {
        Assert.notNull(expression, "Parameter \"expression\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        verifyUnsupportedClass(clazz);
    }

}
