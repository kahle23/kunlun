/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * “Key-Value”形式的注解.<br />
 * @author Kahle
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Kv {

    /**
     * 获取“Key”.<br />
     * @return 获取“Key”
     */
    String k();

    /**
     * 获取“Value”.<br />
     * @return 获取“Value”
     */
    String v();

}
