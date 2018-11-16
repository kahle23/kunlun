package artoria.reflect;

import artoria.entity.Student;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.random.RandomUtils;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class ReflectUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ReflectUtilsTest.class);

    @Test
    public void findConstructors() throws Exception {
        Constructor<?>[] constructors = ReflectUtils.findConstructors(Student.class);
        for (Constructor<?> constructor : constructors) {
            log.info(Arrays.toString(constructor.getParameterTypes()));
        }
    }

    @Test
    public void findFields() throws Exception {
        Student student = RandomUtils.nextObject(Student.class);
        Field[] fields = ReflectUtils.findAccessFields(Student.class);
        for (Field field : fields) {
            ReflectUtils.makeAccessible(field);
            log.info(field.getName() + " | " + field.get(student));
        }
    }

    @Test
    public void findMethods() throws Exception {
        Method[] methods = ReflectUtils.findAccessMethods(Student.class);
        for (Method method : methods) {
            log.info(method.getName());
        }
    }

    @Test
    public void findReadMethodsAndWriteMethods() throws Exception {
        Map<String, Method> readMethods = ReflectUtils.findReadMethods(Student.class);
        for (Map.Entry<String, Method> entry : readMethods.entrySet()) {
            log.info(entry.getKey() + " | " + entry.getValue().getName());
        }

        log.info("------------------------");

        Map<String, Method> writeMethods = ReflectUtils.findWriteMethods(Student.class);
        for (Map.Entry<String, Method> entry : writeMethods.entrySet()) {
            log.info(entry.getKey() + " | " + entry.getValue().getName());
        }
    }

}
