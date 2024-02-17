/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.time;

import static kunlun.common.constant.Numbers.ZERO;

public class SimpleDateTimeFactory implements DateTimeFactory {

    @Override
    public DateTime getInstance() {

        return new SimpleDateTime();
    }

    @Override
    public DateTime getInstance(Long timeInMillis) {
        timeInMillis = timeInMillis != null ? timeInMillis : ZERO;
        DateTime dateTime = new SimpleDateTime();
        dateTime.setTimeInMillis(timeInMillis);
        return dateTime;
    }

}
