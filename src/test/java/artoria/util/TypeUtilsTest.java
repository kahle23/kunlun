package artoria.util;

import artoria.entity.Student;
import org.junit.Test;

import java.lang.reflect.GenericArrayType;

public class TypeUtilsTest {

    @Test
    public void test1() {
        Student[] students = new Student[0];
        GenericArrayType arrayType = TypeUtils.arrayOf(students.getClass());
        System.out.println(arrayType);
    }

}
