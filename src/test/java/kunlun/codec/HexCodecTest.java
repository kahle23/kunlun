/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.util.Arrays;

/**
 * The hex codec tools Test.
 * @author Kahle
 */
public class HexCodecTest {
    private static final Logger log = LoggerFactory.getLogger(HexCodecTest.class);

    @Test
    public void test1() {
        byte[] dataWillEncode1 = {12, 2, 54, -32, 12, 21, 23, -26, -65, 32, 51, 11, -7, 71};
        String dataWillDecode1 = "0C0236E00C1517E6BF20330BF947";
        log.info("byte array: {}", Arrays.toString(dataWillEncode1));
        log.info("string: {}", dataWillDecode1);
        log.info(CodecUtils.encodeToHex(dataWillEncode1));
        log.info(Arrays.toString(CodecUtils.decodeFromHex(dataWillDecode1)));
    }

    @Test
    public void test2() {
        byte[] dataWillEncode2 = {54, -32, 65, -8, 23, -67, 43, 34, -4, 34, -64, 56, 34, 22};
        String dataWillDecode2 = "36E041F817BD2B22FC22C0382216";
        log.info("byte array: {}", Arrays.toString(dataWillEncode2));
        log.info("string: {}", dataWillDecode2);
        log.info(CodecUtils.encodeToHex(dataWillEncode2));
        log.info(Arrays.toString(CodecUtils.decodeFromHex(dataWillDecode2)));
    }

}
