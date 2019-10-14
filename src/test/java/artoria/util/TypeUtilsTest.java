package artoria.util;

import artoria.entity.Student;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.lang.reflect.GenericArrayType;

import static artoria.common.Constants.ZERO;

public class TypeUtilsTest {
    private static Logger log = LoggerFactory.getLogger(TypeUtilsTest.class);

    @Test
    public void test1() {
        Student[] students = new Student[ZERO];
        GenericArrayType arrayType = TypeUtils.arrayOf(students.getClass());
        log.info("{}", arrayType);
    }

}
