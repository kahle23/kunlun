package artoria.db.jdbc;

import artoria.db.jdbc.support.SimpleDataSource;
import artoria.db.jdbc.support.SimpleJdbcDbHandler;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static artoria.common.constant.Numbers.ONE;

@Ignore
public class JdbcDbHandlerTest {
    private static final Logger log = LoggerFactory.getLogger(JdbcDbHandlerTest.class);
    private final JdbcDbHandler jdbcDbHandler = new SimpleJdbcDbHandler(new SimpleDataSource(
            "com.mysql.jdbc.Driver",
            "jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf8&useSSL=false",
            "root",
            "root"
    ));

    @Test
    public void createDbOrTable() {
        String databaseSql = "CREATE DATABASE demo;";
        String tableSql = "CREATE TABLE `t_user` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
                "  `username` varchar(100) NOT NULL COMMENT 'User',\n" +
                "  `password` varchar(200) NOT NULL COMMENT 'Password',\n" +
                "  `email` varchar(200) DEFAULT '' COMMENT 'Email',\n" +
                "  `phone` varchar(20) DEFAULT '' COMMENT 'Phone',\n" +
                "  `remark` varchar(500) DEFAULT '' COMMENT 'Remark',\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='User';";
//        log.info("create db {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(databaseSql)));
//        log.info("create table {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(tableSql)));
    }

    @Test
    public void executeInsert() {
        String sql = "insert into t_user values(?, ?, ?, ?, ?, ?)";
        log.info("insert data {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(sql, new Object[]{
                null, "zhangsan", "123456", "zhangsan@email.com", "", ""
        })));
        log.info("insert data {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(sql, new Object[]{
                null, "lisi", "123456", "lisi@email.com", "", ""
        })));
        log.info("insert data {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(sql, new Object[]{
                null, "wangwu", "123456", "wangwu@email.com", "", ""
        })));
    }

    @Test
    public void executeUpdate() {
        String sql = "update t_user set phone=?, remark=? where id=?;";
        log.info("update data {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(sql, new Object[]{
                "1111", "zs", 1
        })));
        log.info("update data {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(sql, new Object[]{
                "2222", "ls", 2
        })));
        log.info("update data {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(sql, new Object[]{
                "3333", "ww", 3
        })));
    }

    @Test
    public void executeQuery() {
        List<Map<String, Object>> list = jdbcDbHandler.executeQuery(new JdbcQuery("select * from t_user;"));
        for (Map<String, Object> map : list) {
            log.info("{}", map);
        }
    }

    @Test
    public void executeQuery1() {
        List<Map<String, Object>> list = jdbcDbHandler.executeQuery(new JdbcQuery("select * from t_user where id = 1;"));
        log.info("{}", list.get(0));
        list = jdbcDbHandler.executeQuery(new JdbcQuery("select password from t_user where id = 1;"));
        log.info("{} >> {}", list.get(0), list.get(0).get("password"));
        list = jdbcDbHandler.executeQuery(new JdbcQuery("select count(0) from t_user;"));
        log.info("{} >> {}", list.get(0), list.get(0).get("count(0)"));
    }

    @Test
    public void transaction() {
        // SQL info.
        final String sql = "insert into t_user values(?, ?, ?, ?, ?, ?)";
        // Do transaction.
        boolean transaction = jdbcDbHandler.transaction(new JdbcTx(new JdbcAtom() {
            @Override
            public boolean run() throws SQLException {
                // Do insert.
                log.info("> insert data {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(sql, new Object[]{
                        null, "zhaoliu", "123456", "zhaoliu@email.com", "", ""
                })));
                // Do transaction.
                boolean transaction = jdbcDbHandler.transaction(new JdbcTx(new JdbcAtom() {
                    @Override
                    public boolean run() throws SQLException {
                    // Do insert.
                    log.info(">> insert data {}", jdbcDbHandler.executeUpdate(new JdbcUpdate(sql, new Object[]{
                            null, "nestedTransaction", "123456", "nestedTransaction@email.com", "", ""
                    })));
                    // Mock exception.
                    if (true) { throw new RuntimeException("Test throw a exception. "); }
                    return true;
                    }
                }));
                log.info("nestedTransaction: {}", transaction);
                return true;
            }
        }));
        log.info("transaction: {}", transaction);
    }

    @Test
    public void callback() {
        Integer callback = jdbcDbHandler.callback(new JdbcCallback<Integer>() {
            @Override
            public Integer call(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("select count(0) from t_user");
                ResultSet query = statement.executeQuery();
                return query.next() ? query.getInt(ONE) : null;
            }
        });
        log.info("callback {}", callback);
    }

}
