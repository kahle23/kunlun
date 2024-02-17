/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.file;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class PropTest {
    private static Logger log = LoggerFactory.getLogger(PropTest.class);

    @Test
    public void testReadFromClasspath() throws IOException {
        Prop prop = new Prop();
        prop.readFromClasspath("logging.properties");
        log.info("{}", prop.getName());
        log.info("{}", prop.writeToString());
    }

    @Test
    public void testMapable() throws IOException {
        Prop prop = new Prop();
        prop.readFromClasspath("jdbc.properties");
        Map<String, Object> toMap = prop.toMap();
        for (Map.Entry<String, Object> entry : toMap.entrySet()) {
            log.info("{} >> {}", entry.getKey(), entry.getValue());
        }
        toMap.put("password", "test-test");
        prop.fromMap(toMap);
        log.info("{}", prop.writeToString());
    }

}
