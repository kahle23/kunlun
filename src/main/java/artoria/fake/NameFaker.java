package artoria.fake;

import artoria.util.ObjectUtils;
import artoria.util.RandomUtils;
import artoria.util.StringUtils;

import java.util.Map;

import static artoria.common.Constants.*;

public class NameFaker extends AbstractFaker {
    private static final char[] NAME_CHAR_ARRAY = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String FIRST_NAME_CAMEL = "firstName";
    private static final String FIRST_NAME_LINE = "first_name";
    private static final String MIDDLE_NAME_CAMEL = "middleName";
    private static final String MIDDLE_NAME_LINE = "middle_name";
    private static final String LAST_NAME_CAMEL = "lastName";
    private static final String LAST_NAME_LINE = "last_name";
    private static final String FULL_NAME_CAMEL = "fullName";
    private static final String FULL_NAME_LINE = "full_name";
    private static final String ENGLISH_1 = "en";
    private static final String ENGLISH_2 = "eng";
    protected static final String LANG_ATTR_NAME = "lang";

    private String randomName(String language) {
        int length = RandomUtils.nextInt(SIX) + THREE;
        String randomName = RandomUtils.nextString(NAME_CHAR_ARRAY, length);
        return StringUtils.capitalize(randomName);
    }

    /**
     * @param language An ISO 639 language code
     */
    protected boolean isEnglish(String language) {

        return ENGLISH_1.equalsIgnoreCase(language) || ENGLISH_2.equalsIgnoreCase(language);
    }

    protected String firstName(String language) {

        return randomName(language);
    }

    protected String middleName(String language) {
        boolean nextBool = RandomUtils.nextBoolean();
        return nextBool ? randomName(language) : EMPTY_STRING;
    }

    protected String lastName(String language) {

        return randomName(language);
    }

    protected String fullName(String language) {
        String middleName = middleName(language);
        if (StringUtils.isNotBlank(middleName)) {
            middleName = BLANK_SPACE + middleName + BLANK_SPACE;
            return lastName(language) + middleName + firstName(language);
        }
        else {
            return lastName(language) + BLANK_SPACE + firstName(language);
        }
    }

    @Override
    public String name() {

        return "name";
    }

    @Override
    public <T> T fake(String expression, Class<T> clazz) {
        verifyParameters(expression, clazz);
        Map<String, String> params = parseParameters(expression);
        String directive = parseDirective(expression);
        String lang = params.get(LANG_ATTR_NAME);
        if (FIRST_NAME_CAMEL.equals(directive) ||
                FIRST_NAME_LINE.equalsIgnoreCase(directive)) {
            return ObjectUtils.cast(firstName(lang), clazz);
        }
        else if (MIDDLE_NAME_CAMEL.equals(directive) ||
                MIDDLE_NAME_LINE.equalsIgnoreCase(directive)) {
            return ObjectUtils.cast(middleName(lang), clazz);
        }
        else if (LAST_NAME_CAMEL.equals(directive) ||
                LAST_NAME_LINE.equalsIgnoreCase(directive)) {
            return ObjectUtils.cast(lastName(lang), clazz);
        }
        else if (FULL_NAME_CAMEL.equals(directive) ||
                FULL_NAME_LINE.equalsIgnoreCase(directive)) {
            return ObjectUtils.cast(fullName(lang), clazz);
        }
        else {
            return ObjectUtils.cast(fullName(lang), clazz);
        }
    }

}
