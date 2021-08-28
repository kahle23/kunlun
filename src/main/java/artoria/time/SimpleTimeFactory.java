package artoria.time;

import static artoria.common.Constants.ZERO;

public class SimpleTimeFactory implements TimeFactory {

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
