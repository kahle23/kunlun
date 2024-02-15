package artoria.db.jdbc;

import artoria.db.DbUtils;
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
import static artoria.db.jdbc.JdbcDbHandler.*;

@Ignore
public class JdbcDbToolTest {
    private static final Logger log = LoggerFactory.getLogger(JdbcDbToolTest.class);
    private static final String jdbc = "jdbc";

    static {
        SimpleDataSource dataSource = new SimpleDataSource(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf8&useSSL=false",
                "root",
                "root"
        );
        DbUtils.registerHandler(jdbc, new SimpleJdbcDbHandler(dataSource));
    }

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
//        Integer result = DbUtils.execute(jdbc, new JdbcUpdate(databaseSql), EXECUTE_UPDATE, Integer.class);
//        log.info("create db {}", result);
//        result = DbUtils.execute(jdbc, new JdbcUpdate(tableSql), EXECUTE_UPDATE, Integer.class);
//        log.info("create table {}", result);
    }

    @Test
    public void executeInsert() {
        String sql = "insert into t_user values(?, ?, ?, ?, ?, ?)";

        log.info("insert data {}", DbUtils.execute(jdbc, new JdbcUpdate(sql, new Object[]{
                null, "zhangsan", "123456", "zhangsan@email.com", "", "" }), EXECUTE_UPDATE, Integer.class));

        log.info("insert data {}", DbUtils.execute(jdbc, new JdbcUpdate(sql, new Object[]{
                null, "lisi", "123456", "lisi@email.com", "", "" }), EXECUTE_UPDATE, Integer.class));

        log.info("insert data {}", DbUtils.execute(jdbc, new JdbcUpdate(sql, new Object[]{
                null, "wangwu", "123456", "wangwu@email.com", "", "" }), EXECUTE_UPDATE, Integer.class));
    }

    @Test
    public void executeUpdate() {
        String sql = "update t_user set phone=?, remark=? where id=?;";

        log.info("update data {}", DbUtils.execute(jdbc, new JdbcUpdate(
                sql, new Object[]{ "1111", "zs", 1 }), EXECUTE_UPDATE, Integer.class));

        log.info("update data {}", DbUtils.execute(jdbc, new JdbcUpdate(
                sql, new Object[]{ "2222", "ls", 2 }), EXECUTE_UPDATE, Integer.class));

        log.info("update data {}", DbUtils.execute(jdbc, new JdbcUpdate(
                sql, new Object[]{ "3333", "ww", 3 }), EXECUTE_UPDATE, Integer.class));
    }

    @Test
    public void executeQuery() {
        List<Map<String, Object>> list = DbUtils.execute(jdbc
                , new JdbcQuery("select * from t_user;"), EXECUTE_QUERY, List.class);
        for (Map<String, Object> map : list) {
            log.info("{}", map);
        }
    }

    @Test
    public void executeQuery1() {
        List<Map<String, Object>> list = DbUtils.execute(jdbc, new JdbcQuery(
                "select * from t_user where id = 1;"), EXECUTE_QUERY, List.class);
        log.info("{}", list.get(0));

        list = DbUtils.execute(jdbc, new JdbcQuery(
                "select password from t_user where id = 1;"), EXECUTE_QUERY, List.class);
        log.info("{} >> {}", list.get(0), list.get(0).get("password"));

        list = DbUtils.execute(jdbc, new JdbcQuery(
                "select count(0) from t_user;"), EXECUTE_QUERY, List.class);
        log.info("{} >> {}", list.get(0), list.get(0).get("count(0)"));
    }

    @Test
    public void transaction() {
        // SQL info.
        final String sql = "insert into t_user values(?, ?, ?, ?, ?, ?)";
        // Do transaction.
        boolean transaction = DbUtils.execute(jdbc, new JdbcTx(new JdbcAtom() {
            @Override
            public boolean run() throws SQLException {
                // Do insert.
                log.info("> insert data {}", DbUtils.execute(jdbc, new JdbcUpdate(sql, new Object[]{
                        null, "zhaoliu", "123456", "zhaoliu@email.com", "", "" }), EXECUTE_UPDATE, Integer.class));
                // Do transaction.
                boolean transaction = DbUtils.execute(jdbc, new JdbcTx(new JdbcAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        // Do insert.
                        JdbcUpdate jdbcUpdate = new JdbcUpdate(sql, new Object[]{
                                null, "nestedTransaction", "123456", "nestedTransaction@email.com", "", ""});
                        log.info(">> insert data {}", DbUtils.execute(jdbc, jdbcUpdate, EXECUTE_UPDATE, Integer.class));
                        // Mock exception.
                        if (true) { throw new RuntimeException("Test throw a exception. "); }
                        return true;
                    }
                }), TRANSACTION, Boolean.class);
                log.info("nestedTransaction: {}", transaction);
                return true;
            }
        }), TRANSACTION, Boolean.class);
        log.info("transaction: {}", transaction);
    }

    @Test
    public void callback() {
        Integer callback = DbUtils.execute(jdbc, new JdbcCallback<Integer>() {
            @Override
            public Integer call(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("select count(0) from t_user");
                ResultSet query = statement.executeQuery();
                return query.next() ? query.getInt(ONE) : null;
            }
        }, CALLBACK, Integer.class);
        log.info("callback {}", callback);
    }

}
