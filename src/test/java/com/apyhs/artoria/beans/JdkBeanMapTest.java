package com.apyhs.artoria.beans;

import com.alibaba.fastjson.JSON;
import com.apyhs.artoria.entity.EntityUtils;
import com.apyhs.artoria.entity.Student;
import org.junit.Test;

public class JdkBeanMapTest {
    private Student student = EntityUtils.getZhangSan();

    @Test
    public void test1() {
        BeanMap map = new JdkBeanMap();
        map.setBean(student);
        System.out.println(JSON.toJSONString(student));
        System.out.println(map);
        System.out.println(map.put("name", "lisi"));
        System.out.println(map);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        BeanMap map = new JdkBeanMap(student);
        System.out.println(map);

        BeanMap newMap = (JdkBeanMap) map.clone();
        System.out.println(student == newMap.getBean());
        newMap.put("name", "lisi");
        System.out.println(newMap);
    }

}
