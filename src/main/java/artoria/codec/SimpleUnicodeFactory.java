package artoria.codec;

public class SimpleUnicodeFactory implements UnicodeFactory {
    private static final Unicode UNICODE = new Unicode();

    @Override
    public Unicode getInstance() {

        return UNICODE;
    }

}
