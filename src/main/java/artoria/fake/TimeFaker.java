package artoria.fake;

import artoria.convert.type.TypeConvertUtils;
import artoria.time.DateTime;
import artoria.time.DateUtils;
import artoria.util.ObjectUtils;
import artoria.util.RandomUtils;

import java.util.Date;

import static artoria.common.Constants.*;

public class TimeFaker extends AbstractFaker {
    private Integer bound;

    public TimeFaker() {

        this(FIVE);
    }

    public TimeFaker(Integer bound) {
        if (bound == null || bound <= ZERO) {
            throw new IllegalArgumentException(
                    "Parameter \"bound\" must not null and greater than 0. "
            );
        }
        this.bound = bound;
    }

    @Override
    public String name() {

        return "time";
    }

    @Override
    public <T> T fake(String expression, Class<T> clazz) {
        verifyParameters(expression, clazz);
        int minYear = DateUtils.getYear(new Date()) - bound;
        int year = RandomUtils.nextInt(bound * TWO) + minYear;
        int month = RandomUtils.nextInt(THIRTEEN);
        int day = RandomUtils.nextInt(THIRTY_ONE);
        int hour = RandomUtils.nextInt(TWENTY_FOUR);
        int minute = RandomUtils.nextInt(SIXTY);
        int second = RandomUtils.nextInt(SIXTY);
        int millisecond = RandomUtils.nextInt(NINE_HUNDRED_NINETY_NINE);
        DateTime dateTime = DateUtils.create(
                year, month, day, hour, minute, second, millisecond
        );
        Date date = dateTime.getDate();
        Object convert = TypeConvertUtils.convert(date, clazz);
        return ObjectUtils.cast(convert, clazz);
    }

}
