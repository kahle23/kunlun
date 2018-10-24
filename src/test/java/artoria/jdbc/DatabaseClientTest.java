package artoria.jdbc;

import artoria.entity.User;
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
    private DataSource dataSource = new SimpleDataSource();
    private DatabaseClient dbClient = new DatabaseClient(dataSource);

    @Test
    public void getTableMeta() throws Exception {
        List<TableMeta> tableMetaList = dbClient.getTableMetaList();
        for (TableMeta tableMeta : tableMetaList) {
            System.out.println("--------");
            System.out.println(tableMeta.getName() + " | " + tableMeta.getRemarks());
            System.out.println(tableMeta.getPrimaryKey());
            for (ColumnMeta columnMeta : tableMeta.getColumnMetaList()) {
                System.out.println(JSON.toJSONString(columnMeta, true));
            }
            System.out.println("--------");
            System.out.println();
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
        System.out.println(execute);
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
                System.out.println("nestedTransaction: " + transaction);
                return true;
            }
        });
        System.out.println("transaction: " + transaction);
    }

    @Test
    public void update() throws Exception {
        int i = dbClient.update("insert into t_user values(?, ?, ?, ?, ?, ?)", 1, "zhangsan", 19, "zhangsan@email.com", "", "");
        System.out.println(i);
    }

    @Test
    public void query() throws Exception {
        List<Map<String, Object>> list = dbClient.query("select * from t_user");
        // List<Map<String, Object>> list = dbClient.query("select * from t_user where name = ?", "zhangsan");
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
    }

    @Test
    public void query1() throws Exception {
        List<User> users = dbClient.query(User.class, "select * from t_user");
        // List<User> users = dbClient.query(User.class, "select * from t_user where name = ?", "zhangsan");
        for (User user : users) {
            System.out.println(JSON.toJSONString(user));
        }
    }

    @Test
    public void queryFirst() throws Exception {
        Map<String, Object> map = dbClient.queryFirst("select * from t_user");
        // Map<String, Object> map = dbClient.queryFirst("select * from t_user where name = ?", "zhangsan");
        System.out.println(map);
    }

    @Test
    public void queryFirst1() throws Exception {
        User user = dbClient.queryFirst(User.class, "select * from t_user");
        // User user = dbClient.queryFirst(User.class, "select * from t_user where name = ?", "zhangsan");
        System.out.println(JSON.toJSONString(user));
    }

}
