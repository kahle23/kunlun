/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common.constant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * The common null (nil) constants.
 * @author Kahle
 */
public class Nil {

    // region ======== null methods ========

    public static <T> T get() { return null; }

    public static <T> T g() { return get(); }
    // endregion ======== null methods ========


    // region ======== common objects ========

    public static final Class<?> CLZ = null;
    public static final Object OBJ = null;
    public static final Date DATE = null;
    public static final Character CHAR = null;
    public static final Boolean BOOL = null;
    public static final String STR = null;
    // endregion ======== common objects ========


    // region ======== numbers ========

    public static final Integer INT = null;
    public static final Byte INT1 = null;
    public static final Short INT2 = null;
    public static final Long INT3 = null;
    public static final BigInteger INT6 = null;
    public static final Float FLT = null;
    public static final Double FLT1 = null;
    public static final BigDecimal FLT6 = null;
    // endregion ======== numbers ========


    private Nil() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
