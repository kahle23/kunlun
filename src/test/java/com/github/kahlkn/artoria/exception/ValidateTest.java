package com.github.kahlkn.artoria.exception;

import com.github.kahlkn.artoria.logging.Logger;
import com.github.kahlkn.artoria.logging.LoggerFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ValidateTest {
    private Logger log = LoggerFactory.getLogger(ValidateTest.class);

    @Test
    public void test1() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            Validate.notEmpty(data
                    , SystemCode.code1
                    , "Data should not null in here. "
            );
        }
        catch (BusinessException e) {
            log.error(e);
        }
    }

    @Test
    public void test2() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            Validate.notEmpty(data
                    , "Please input something first."
                    , "Data should not null in here. "
            );
        }
        catch (BusinessException e) {
            log.error(e);
        }
    }

}
