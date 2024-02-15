package artoria.time;

import static artoria.common.constant.Numbers.ZERO;

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
