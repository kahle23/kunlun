package artoria.exchange;

import artoria.entity.Student;
import artoria.random.RandomUtils;
import artoria.util.TypeUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtilsTest {
    private Student data = new Student();
    private List<Student> data1 = new ArrayList<Student>();
    private Map<Long, Student> data2 = new HashMap<Long, Student>();
    private String jsonString = null;
    private String jsonString1 = null;
    private String jsonString2 = null;

    @Before
    public void init() {
        JsonUtils.setJsonProvider(new SimpleJsonProvider(true) {
            @Override
            public <T> T parseObject(String text,Type clazz) {
                return JSON.parseObject(text, clazz);
            }
            @Override
            public String toJsonString(Object object) {
                return JSON.toJSONString(object, this.getPrettyFormat());
            }
        });
        data = RandomUtils.nextObject(Student.class);
        for (int i = 0; i < 5; i++) {
            Student student = RandomUtils.nextObject(Student.class);
            data1.add(student);
            data2.put(student.getStudentId(), student);
        }
        jsonString = JsonUtils.toJsonString(data);
        jsonString1 = JsonUtils.toJsonString(data1);
        jsonString2 = JsonUtils.toJsonString(data2);
    }

    @Test
    public void test0() {
        System.out.println(JsonUtils.toJsonString(data));
        System.out.println(JsonUtils.toJsonString(data1));
        System.out.println(JsonUtils.toJsonString(data2));
    }

    @Test
    public void test1() {
        Student student = JsonUtils.parseObject(jsonString, Student.class);
        System.out.println(JsonUtils.toJsonString(student));
    }

    @Test
    public void test2() {
        List<Student> list = JsonUtils.parseObject(jsonString1
                , TypeUtils.parameterizedOf(List.class, Student.class));
        System.out.println(JsonUtils.toJsonString(list));
    }

    @Test
    public void test3() {
        Map<Long, Student> map = JsonUtils.parseObject(jsonString2
                , TypeUtils.parameterizedOf(Map.class, Long.class, Student.class));
        System.out.println(JsonUtils.toJsonString(map));
    }

}
