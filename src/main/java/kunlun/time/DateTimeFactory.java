/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.time;

public interface DateTimeFactory {

    DateTime getInstance();

    DateTime getInstance(Long timeInMillis);

}
