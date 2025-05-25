/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MessageListener.
 * @author Kahle
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageListener {

    /**
     * manager
     * @return manager
     */
    String manager() default "internal-bus";

    /**
     * topic
     * @return topic
     */
    String topic();

    /**
     * subExpression
     * @return subExpression
     */
    String subExpression() default "*";

}
