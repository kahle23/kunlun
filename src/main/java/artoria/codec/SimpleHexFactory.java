package artoria.codec;

public class SimpleHexFactory implements HexFactory {
    private static final Hex LOWER_CASE_HEX = new Hex(true) {
        @Override
        public void setLowerCase(boolean lowerCase) {
            throw new UnsupportedOperationException();
        }
    };
    private static final Hex UPPER_CASE_HEX = new Hex(false) {
        @Override
        public void setLowerCase(boolean lowerCase) {
            throw new UnsupportedOperationException();
        }
    };

    @Override
    public Hex getInstance() {

        return getInstance(true);
    }

    @Override
    public Hex getInstance(boolean lowerCase) {

        return lowerCase ? LOWER_CASE_HEX : UPPER_CASE_HEX;
    }

}
