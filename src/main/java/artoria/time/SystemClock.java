package artoria.time;

/**
 * System clock.
 * @see System#currentTimeMillis()
 * @author Kahle
 */
public class SystemClock implements Clock {

    @Override
    public long getTime() {

        return System.currentTimeMillis();
    }

}
