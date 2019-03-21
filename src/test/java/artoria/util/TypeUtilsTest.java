package artoria.util;

import artoria.entity.Student;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.lang.reflect.GenericArrayType;

public class TypeUtilsTest {
    private static Logger log = LoggerFactory.getLogger(TypeUtilsTest.class);

    @Test
    public void test1() {
        Student[] students = new Student[0];
        GenericArrayType arrayType = TypeUtils.arrayOf(students.getClass());
        log.info("{}", arrayType);
    }

}
