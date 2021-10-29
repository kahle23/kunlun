package artoria.time;

public interface DateTimeFactory {

    DateTime getInstance();

    DateTime getInstance(Long timeInMillis);

}
