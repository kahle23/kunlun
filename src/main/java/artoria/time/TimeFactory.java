package artoria.time;

public interface TimeFactory {

    DateTime getInstance();

    DateTime getInstance(Long timeInMillis);

}
