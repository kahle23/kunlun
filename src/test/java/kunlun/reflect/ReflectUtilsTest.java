/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect;

import kunlun.data.mock.MockUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectUtilsTest {
    private static Logger log = LoggerFactory.getLogger(ReflectUtilsTest.class);

    @Test
    public void testFindConstructors() throws Exception {
        Constructor<?>[] constructors = ReflectUtils.getConstructors(User.class);
        for (Constructor<?> constructor : constructors) {
            log.info(Arrays.toString(constructor.getParameterTypes()));
        }
    }

    @Test
    public void testFindFields() throws Exception {
        User user = MockUtils.mock(User.class);
        Field[] fields = ReflectUtils.getAccessibleFields(User.class);
        for (Field field : fields) {
            ReflectUtils.makeAccessible(field);
            log.info("{} | {}", field.getName(), field.get(user));
        }
    }

    @Test
    public void testFindMethods() throws Exception {
        Method[] methods = ReflectUtils.getAccessibleMethods(User.class);
        for (Method method : methods) {
            log.info(method.getName());
        }
    }

    @Test
    public void testFindPropertyDescriptors() throws Exception {
        PropertyDescriptor[] descriptors = ReflectUtils.getPropertyDescriptors(User.class);
        for (PropertyDescriptor descriptor : descriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            Method readMethod = descriptor.getReadMethod();
            String shortDescription = descriptor.getShortDescription();
            String displayName = descriptor.getDisplayName();
            String name = descriptor.getName();
            Class<?> propertyType = descriptor.getPropertyType();
            log.info("----------------");
            log.info("Short description: {}", shortDescription);
            log.info("Display name: {}", displayName);
            log.info("Name: {}", name);
            log.info("Property type: {}", propertyType);
            log.info("Write method: {}", writeMethod);
            log.info("Read method: {}", readMethod);
            log.info("----------------");
        }
    }

}
