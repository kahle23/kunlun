package kunlun.extension.convert.exchangerate.annotation;

import java.lang.annotation.*;

/**
 * 汇率注解.
 * 如果不填，或者报错，默认不抛异常
 * @author Kahle
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExRate {

    /**
     * 基础币种对应的金额字段
     * <li>如果该字段有值，表示走金额转换</li>
     * <li>如果该字段无值，表示走汇率填写</li>
     *
     * @return 基础币种对应的金额字段
     */
    String amountField() default "";

    /**
     * 汇率数据的来源标识.
     * @return 汇率数据的来源标识
     */
    String source() default "";

    /**
     * 基础币种的值（和 baseField 二选一，优先走值）
     * @return 基础币种的值
     */
    String base() default "";

    /**
     * 基础币种的字段（和 base 二选一，优先走值）
     * @return 基础币种的字段
     */
    String baseField() default "";

    /**
     * 目标币种的值（和 targetField 二选一，优先走值）
     * @return 目标币种的值
     */
    String target() default "";

    /**
     * 目标币种的字段（和 target 二选一，优先走值）
     * @return 目标币种的字段
     */
    String targetField() default "";

    /**
     * 汇率的时间字段（默认为当前时间）
     * @return 汇率的时间字段
     */
    String timeField() default "";

}
