package artoria.codec;

public interface Base64Factory {

    Base64 getInstance();

    Base64 getInstance(boolean urlSafe);

    Base64 getInstance(boolean mime, int lineLength, byte[] lineSeparator);

}
