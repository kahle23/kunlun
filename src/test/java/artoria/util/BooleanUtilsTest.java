package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class BooleanUtilsTest {
    private static Logger log = LoggerFactory.getLogger(BooleanUtilsTest.class);

    @Test
    public void parseBooleanTest() {
        log.info("\"0\" parseBoolean: {}", BooleanUtils.parseBoolean("0"));
        log.info("\"1\" parseBoolean: {}", BooleanUtils.parseBoolean("1"));
        log.info("\"YeS\" parseBoolean: {}", BooleanUtils.parseBoolean("YeS"));
        log.info("\"nO\" parseBoolean: {}", BooleanUtils.parseBoolean("nO"));
        log.info("\"y\" parseBoolean: {}", BooleanUtils.parseBoolean("y"));
        log.info("\"N\" parseBoolean: {}", BooleanUtils.parseBoolean("N"));
        log.info("\"T\" parseBoolean: {}", BooleanUtils.parseBoolean("T"));
        log.info("\"f\" parseBoolean: {}", BooleanUtils.parseBoolean("f"));
        log.info("\"oN\" parseBoolean: {}", BooleanUtils.parseBoolean("oN"));
        log.info("\"oFf\" parseBoolean: {}", BooleanUtils.parseBoolean("oFf"));
        log.info("\"trUe\" parseBoolean: {}", BooleanUtils.parseBoolean("trUe"));
        log.info("\"faLse\" parseBoolean: {}", BooleanUtils.parseBoolean("faLse"));
        log.info("\"AAaAA\" parseBoolean: {}", BooleanUtils.parseBoolean("AAaAA"));
        log.info("\"rrr\" parseBoolean: {}", BooleanUtils.parseBoolean("rrr"));
        log.info("\"null\" parseBoolean: {}", BooleanUtils.parseBoolean(null));
        log.info("\"\" parseBoolean: {}", BooleanUtils.parseBoolean(""));
    }

    @Test
    public void valueOfTest() {
        log.info("\"0\" valueOf: {}", BooleanUtils.valueOf("0"));
        log.info("\"1\" valueOf: {}", BooleanUtils.valueOf("1"));
        log.info("\"YeS\" valueOf: {}", BooleanUtils.valueOf("YeS"));
        log.info("\"nO\" valueOf: {}", BooleanUtils.valueOf("nO"));
        log.info("\"y\" valueOf: {}", BooleanUtils.valueOf("y"));
        log.info("\"N\" valueOf: {}", BooleanUtils.valueOf("N"));
        log.info("\"T\" valueOf: {}", BooleanUtils.valueOf("T"));
        log.info("\"f\" valueOf: {}", BooleanUtils.valueOf("f"));
        log.info("\"oN\" valueOf: {}", BooleanUtils.valueOf("oN"));
        log.info("\"oFf\" valueOf: {}", BooleanUtils.valueOf("oFf"));
        log.info("\"trUe\" valueOf: {}", BooleanUtils.valueOf("trUe"));
        log.info("\"faLse\" valueOf: {}", BooleanUtils.valueOf("faLse"));
        log.info("\"AAaAA\" valueOf: {}", BooleanUtils.valueOf("AAaAA"));
        log.info("\"rrr\" valueOf: {}", BooleanUtils.valueOf("rrr"));
        log.info("\"null\" valueOf: {}", BooleanUtils.valueOf(null));
        log.info("\"\" valueOf: {}", BooleanUtils.valueOf(""));
    }

}
