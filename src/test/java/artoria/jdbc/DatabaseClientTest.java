package artoria.jdbc;

import artoria.entity.User;
import artoria.exception.ExceptionUtils;
import artoria.file.Prop;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Ignore
public class DatabaseClientTest {
    private static Logger log = LoggerFactory.getLogger(DatabaseClientTest.class);
    private DatabaseClient dbClient;
    private DataSource dataSource;

    {
        try {
            Prop prop = new Prop();
            prop.readFromClasspath("jdbc.properties");
            dataSource = new SimpleDataSource(prop.getProperties());
            dbClient = new DatabaseClient(dataSource);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Test
    public void getTableMeta() throws Exception {
        List<TableMeta> tableMetaList = dbClient.getTableMetaList();
        for (TableMeta tableMeta : tableMetaList) {
            log.info("--------");
            log.info("{} | {}", tableMeta.getName(), tableMeta.getRemarks());
            log.info(tableMeta.getPrimaryKey());
            for (ColumnMeta columnMeta : tableMeta.getColumnMetaList()) {
                log.info(JSON.toJSONString(columnMeta, true));
            }
            log.info("--------");
            log.info("");
        }
    }

    @Test
    public void execute() throws Exception {
        Integer execute = dbClient.execute(new DatabaseCallback<Integer>() {
            @Override
            public Integer call(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("select count(0) from t_user");
                ResultSet query = statement.executeQuery();
                return query.next() ? query.getInt(1) : null;
            }
        });
        log.info("{}", execute);
    }

    @Test
    public void transaction() throws Exception {
        boolean transaction = dbClient.transaction(new DatabaseAtom() {
            @Override
            public boolean run() throws SQLException {
                dbClient.update("insert into t_user values(?, ?, ?, ?, ?, ?)", 997, "transaction", 19, "transaction@email.com", "", "");
                boolean transaction = dbClient.transaction(new DatabaseAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        dbClient.update("insert into t_user values(?, ?, ?, ?, ?, ?)", 998, "nestedTransaction", 19, "nestedTransaction@email.com", "", "");
                        // if (true) { throw new RuntimeException("Test throw a exception. "); }
                        return true;
                    }
                });
                log.info("nestedTransaction: {}", transaction);
                return true;
            }
        });
        log.info("transaction: {}", transaction);
    }

    @Test
    public void update() throws Exception {
        int i = dbClient.update("insert into t_user values(?, ?, ?, ?, ?, ?)", 1, "zhangsan", 19, "zhangsan@email.com", "", "");
        log.info("{}", i);
    }

    @Test
    public void query() throws Exception {
        List<Map<String, Object>> list = dbClient.query("select * from t_user");
        // List<Map<String, Object>> list = dbClient.query("select * from t_user where name = ?", "zhangsan");
        for (Map<String, Object> map : list) {
            log.info(map.toString());
        }
    }

    @Test
    public void query1() throws Exception {
        List<User> users = dbClient.query(User.class, "select * from t_user");
        // List<User> users = dbClient.query(User.class, "select * from t_user where name = ?", "zhangsan");
        for (User user : users) {
            log.info(JSON.toJSONString(user));
        }
    }

    @Test
    public void queryFirst() throws Exception {
        Map<String, Object> map = dbClient.queryFirst("select * from t_user");
        // Map<String, Object> map = dbClient.queryFirst("select * from t_user where name = ?", "zhangsan");
        log.info("{}", map);
    }

    @Test
    public void queryFirst1() throws Exception {
        User user = dbClient.queryFirst(User.class, "select * from t_user");
        // User user = dbClient.queryFirst(User.class, "select * from t_user where name = ?", "zhangsan");
        log.info(JSON.toJSONString(user));
    }

}
