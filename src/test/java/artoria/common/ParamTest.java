package artoria.common;

import artoria.entity.Student;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.random.RandomUtils;
import artoria.time.DateUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Date;

public class ParamTest {
    private static Logger log = LoggerFactory.getLogger(ParamTest.class);

    @Test
    public void test1() {
        Param<Date> param = new Param<Date>();
        param.setCurrentUser(RandomUtils.nextObject(Student.class));
        param.setPaging(new Paging(1, 15));
        param.setData(new Date());
        Student currentUser = (Student) param.getCurrentUser();
        Paging paging = param.getPaging();
        Date data = param.getData();
        log.info(JSON.toJSONString(currentUser));
        log.info("" + paging);
        log.info(DateUtils.format(data));
    }

}
