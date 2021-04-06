package artoria.time;

public class SimpleDateTimeFactory implements DateTimeFactory {

    @Override
    public DateTime getInstance() {

        return new SimpleDateTime();
    }

    @Override
    public DateTime getInstance(Long timeInMillis) {
        DateTime dateTime = new SimpleDateTime();
        dateTime.setTimeInMillis(timeInMillis);
        return dateTime;
    }

}
