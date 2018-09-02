package artoria.exchange;

import artoria.entity.Student;
import artoria.random.RandomUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JsonUtilsTest {
    private Student data = new Student();
    private List<Student> data1 = new ArrayList<Student>();
    private String jsonString = null;
    private String jsonString1 = null;

    @Before
    public void init() {
        JsonUtils.setJsonHandler(new JsonHandler() {
            @Override
            public <T> T parseObject(String text, Class<T> clazz) {

                return JSON.parseObject(text, clazz);
            }
            @Override
            public <T> List<T> parseArray(String text, Class<T> clazz) {

                return JSON.parseArray(text, clazz);
            }
            @Override
            public String toJsonString(Object object) {

                return JSON.toJSONString(object);
            }
            @Override
            public String toJsonString(Object object, boolean prettyFormat) {

                return JSON.toJSONString(object, prettyFormat);
            }
        });
        data = RandomUtils.nextObject(Student.class);
        for (int i = 0; i < 5; i++) {
            data1.add(RandomUtils.nextObject(Student.class));
        }
        jsonString = JsonUtils.toJsonString(data);
        jsonString1 = JsonUtils.toJsonString(data1);
    }

    @Test
    public void test1() {
        System.out.println(JsonUtils.toJsonString(data));
        System.out.println(JsonUtils.toJsonString(data1));
        System.out.println(JsonUtils.toJsonString(data, true));
    }

    @Test
    public void test2() {
        Student student = JsonUtils.parseObject(jsonString, Student.class);
        List<Student> list = JsonUtils.parseArray(jsonString1, Student.class);
        System.out.println(JsonUtils.toJsonString(student));
        System.out.println(JsonUtils.toJsonString(list));
    }

}
