package artoria.common;

import artoria.entity.Student;
import artoria.random.RandomUtils;
import artoria.time.DateUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Date;

public class ParamTest {

    @Test
    public void test1() {
        Param<Date> param = new Param<Date>();
        param.setCurrentUser(RandomUtils.nextObject(Student.class));
        param.setPaging(new Paging(1, 15));
        param.setData(new Date());
        Student currentUser = param.getCurrentUser();
        Paging paging = param.getPaging();
        Date data = param.getData();
        System.out.println(JSON.toJSONString(currentUser));
        System.out.println(paging);
        System.out.println(DateUtils.format(data));
    }

}
