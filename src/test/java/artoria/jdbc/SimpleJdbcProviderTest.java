package artoria.jdbc;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.test.pojo.entity.system.User;
import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.*;

@Deprecated
@Ignore
public class SimpleJdbcProviderTest {
    private static Logger log = LoggerFactory.getLogger(SimpleJdbcProviderTest.class);
    private JdbcProvider jdbcProvider = new SimpleJdbcProvider(new SimpleDataSource());

    @Test
    public void transaction() throws Exception {
        boolean transaction = jdbcProvider.transaction(new JdbcAtom() {
            @Override
            public boolean run() throws SQLException {
                jdbcProvider.executeUpdate("insert into t_user values(?, ?, ?, ?, ?, ?)"
                        , NINETY_NINE, "transaction", TWENTY, "transaction@email.com", "", "");
                boolean transaction = jdbcProvider.transaction(new JdbcAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        jdbcProvider.executeUpdate("insert into t_user values(?, ?, ?, ?, ?, ?)"
                                , ONE_HUNDRED, "nestedTransaction", TWENTY, "nestedTransaction@email.com", "", "");
                        // if (true) { throw new RuntimeException("Test throw a exception. "); }
                        return true;
                    }
                }, null);
                log.info("nestedTransaction: {}", transaction);
                return true;
            }
        }, null);
        log.info("transaction: {}", transaction);
    }

    @Test
    public void executeQuery() throws Exception {
        List<Map<String, Object>> list = jdbcProvider.executeQuery("select * from t_user");
        // List<Map<String, Object>> list = jdbcProvider.executeQuery("select * from t_user where name = ?", "zhangsan");
        for (Map<String, Object> map : list) {
            log.info("{}", map);
        }
    }

    @Test
    public void executeQuery1() throws Exception {
        List<User> users = jdbcProvider.executeQuery("select * from t_user", User.class);
//         List<User> users = jdbcProvider.executeQuery("select * from t_user where name = ?", User.class, "zhangsan");
        for (User user : users) {
            log.info(JSON.toJSONString(user));
        }
    }

    @Test
    public void executeUpdate() throws Exception {
        int i = jdbcProvider.executeUpdate("insert into t_user values(?, ?, ?, ?, ?, ?)"
                , ONE, "zhangsan", TWENTY, "zhangsan@email.com", "", "");
        log.info("{}", i);
    }

    @Test
    public void execute() throws Exception {
        Integer execute = jdbcProvider.execute(new JdbcCallback<Integer>() {
            @Override
            public Integer call(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("select count(0) from t_user");
                ResultSet query = statement.executeQuery();
                return query.next() ? query.getInt(ONE) : null;
            }
        });
        log.info("{}", execute);
    }

}
