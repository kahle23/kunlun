package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.test.pojo.entity.system.User;
import org.junit.Test;

import java.lang.reflect.GenericArrayType;

import static artoria.common.constant.Numbers.ZERO;

public class TypeUtilsTest {
    private static Logger log = LoggerFactory.getLogger(TypeUtilsTest.class);

    @Test
    public void test1() {
        User[] users = new User[ZERO];
        GenericArrayType arrayType = TypeUtils.arrayOf(users.getClass());
        log.info("{}", arrayType);
    }

}
