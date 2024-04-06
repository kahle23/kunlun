/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.support;

import kunlun.common.constant.Nulls;
import kunlun.core.Loader;
import kunlun.core.function.Consumer;
import kunlun.db.jdbc.meta.Column;
import kunlun.db.jdbc.meta.Table;
import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;
import kunlun.util.CloseUtils;
import kunlun.util.CollectionUtils;
import kunlun.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.Symbols.COMMA;
import static kunlun.common.constant.Symbols.SINGLE_QUOTE;
import static kunlun.util.CollectionUtils.isNotEmpty;

/**
 * The jdbc based table structure information loader.
 * @author Kahle
 */
public class JdbcTableLoader implements Loader<JdbcTableLoader.Config, List<Table>> {
    protected static final String TABLE_NAME  = "TABLE_NAME";
    protected static final String TABLE       = "TABLE";
    protected static final String REMARKS     = "REMARKS";
    protected static final String COLUMN_NAME = "COLUMN_NAME";
    protected static final String TYPE_NAME   = "TYPE_NAME";
    protected static final String IS_NULLABLE = "IS_NULLABLE";
    protected static final String COLUMN_SIZE = "COLUMN_SIZE";
    protected static final String DECIMAL_DIGITS   = "DECIMAL_DIGITS";
    protected static final String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";
    protected static final String YES = "YES";

    @Override
    public List<Table> load(Config config) {
        // Get parameters and validate.
        Assert.notNull(config, "Parameter \"config\" must not null. ");
        DataSource dataSource = config.getDataSource();
        String url      = config.getUrl();
        String username = config.getUsername();
        String password = config.getPassword();
        String catalog  = config.getCatalog();
        Assert.notBlank(catalog, "Parameter \"catalog\" must not blank. ");
        if (dataSource == null) {
            Assert.notBlank(url     , "Parameter \"url\" must not blank. ");
            Assert.notBlank(username, "Parameter \"username\" must not blank. ");
            Assert.notNull( password, "Parameter \"password\" must not null. ");
        }
        // Do load.
        Connection connection = null;
        try {
            // Get the connection.
            connection = dataSource != null
                    ? dataSource.getConnection() : DriverManager.getConnection(url, username, password);
            // Load tables information.
            List<Table> tables = loadTables(
                    connection, catalog, config.getReservedTables(), config.getExcludedTables());
            // Process the post consumers.
            if (CollectionUtils.isNotEmpty(config.getPostConsumers())) {
                Context context = new Context(config, connection, catalog, tables);
                for (Consumer<Context> c : config.getPostConsumers()) { c.accept(context); }
            }
            // Finish.
            return tables;
        }
        catch (Exception e) { throw ExceptionUtils.wrap(e); }
        finally { CloseUtils.closeQuietly(connection); }
    }

    public List<Table> loadTables(Connection connection, String catalog
            , Collection<String> reservedTables, Collection<String> excludedTables) throws SQLException {
        // Validate and declare.
        Assert.notNull(connection, "Parameter \"connection\" must not null. ");
        List<Table> tables = new ArrayList<Table>();
        String[]    types  = new String[]{TABLE};
        ResultSet tableRs  = null;
        // Do load tables.
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            tableRs = dbMetaData.getTables(catalog, Nulls.STR, Nulls.STR, types);
            while (tableRs.next()) {
                String tableName = tableRs.getString(TABLE_NAME);
                String remarks = tableRs.getString(REMARKS);
                // Judge reserved tables and excluded tables.
                if (isNotEmpty(reservedTables) && !reservedTables.contains(tableName)) {
                    continue;
                }
                if (isNotEmpty(excludedTables) && excludedTables.contains(tableName)) {
                    continue;
                }
                // Create table object.
                Table table = new Table();
                table.setName(tableName);
                table.setComment(remarks);
                table.setColumns(new ArrayList<Column>());
                tables.add(table);
                fillColumns(dbMetaData, catalog, table);
            }
            return tables;
        }
        finally { CloseUtils.closeQuietly(tableRs); }
    }

    protected void fillColumns(DatabaseMetaData dbMetaData, String catalog, Table table) throws SQLException {
        String tableName = table.getName();
        ResultSet columnRs = null;
        try {
            // Get primary keys.
            String primaryKeys = getPrimaryKeys(dbMetaData, catalog, tableName);
            List<String> primaryKeyList = Arrays.asList(primaryKeys.split(COMMA));
            table.setPrimaryKeys(primaryKeys);
            // Loop column result set and build column object.
            columnRs = dbMetaData.getColumns(catalog, Nulls.STR, tableName, Nulls.STR);
            while (columnRs.next()) {
                table.getColumns().add(buildColumn(columnRs, primaryKeyList));
            }
        }
        finally { CloseUtils.closeQuietly(columnRs); }
    }

    protected String getPrimaryKeys(DatabaseMetaData dbMetaData, String catalog, String table) throws SQLException {
        StringBuilder pkBuilder = new StringBuilder();
        ResultSet pkResultSet = null;
        try {
            pkResultSet = dbMetaData.getPrimaryKeys(catalog, Nulls.STR, table);
            while (pkResultSet.next()) {
                pkBuilder.append(pkResultSet.getString(COLUMN_NAME)).append(COMMA);
            }
            if (pkBuilder.length() > ZERO) {
                pkBuilder.deleteCharAt(pkBuilder.length() - ONE);
            }
            return pkBuilder.toString();
        }
        finally { CloseUtils.closeQuietly(pkResultSet); }
    }

    protected Column buildColumn(ResultSet columnRs, List<String> primaryKeys) throws SQLException {
        // Get parameters.
        String columnName = columnRs.getString(COLUMN_NAME);
        // "IS_NULLABLE" is "NO" or "YES".
        String isNullable = columnRs.getString(IS_NULLABLE);
        // "IS_AUTOINCREMENT" is "NO" or "YES".
        String isAutoincrement = columnRs.getString(IS_AUTOINCREMENT);
        // Judge nullable and autoincrement.
        Boolean nullable = null, autoincrement = null;
        if (StringUtils.isNotBlank(isNullable)) {
            nullable = YES.equalsIgnoreCase(isNullable);
        }
        if (StringUtils.isNotBlank(isAutoincrement)) {
            autoincrement = YES.equalsIgnoreCase(isAutoincrement);
        }
        // Create column object.
        Column column = new Column();
        column.setName(columnName);
        //column.setOrder();
        column.setType(columnRs.getString(TYPE_NAME));
        column.setSize(columnRs.getInt(COLUMN_SIZE));
        column.setDecimalDigits(columnRs.getInt(DECIMAL_DIGITS));
        column.setNullable(nullable);
        //column.setDefaultValue();
        column.setComment(columnRs.getString(REMARKS));
        column.setPrimaryKey(primaryKeys.contains(columnName));
        column.setAutoincrement(autoincrement);
        return column;
    }

    /**
     * The configuration of table structure information loader.
     * @author Kahle
     */
    public static class Config {
        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private String catalog;
        private DataSource dataSource;
        private final Collection<String> reservedTables = new ArrayList<String>();
        private final Collection<String> excludedTables = new ArrayList<String>();
        private final Collection<Consumer<Context>> postConsumers = new ArrayList<Consumer<Context>>();

        public String getDriverClassName() {

            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {

            this.driverClassName = driverClassName;
        }

        public String getUrl() {

            return url;
        }

        public void setUrl(String url) {

            this.url = url;
        }

        public String getUsername() {

            return username;
        }

        public void setUsername(String username) {

            this.username = username;
        }

        public String getPassword() {

            return password;
        }

        public void setPassword(String password) {

            this.password = password;
        }

        public String getCatalog() {

            return catalog;
        }

        public void setCatalog(String catalog) {

            this.catalog = catalog;
        }

        public DataSource getDataSource() {

            return dataSource;
        }

        public void setDataSource(DataSource dataSource) {

            this.dataSource = dataSource;
        }

        public Collection<String> getReservedTables() {

            return reservedTables;
        }

        public Collection<String> getExcludedTables() {

            return excludedTables;
        }

        public Collection<Consumer<Context>> getPostConsumers() {

            return postConsumers;
        }
    }

    /**
     * The context of the post-consumers for table structure information loader.
     * @author Kahle
     */
    public static class Context implements kunlun.core.Context {
        private final Config config;
        private final Connection connection;
        private final String catalog;
        private final List<Table> tables;

        public Context(Config config, Connection connection, String catalog, List<Table> tables) {
            this.connection = connection;
            this.catalog = catalog;
            this.tables = tables;
            this.config = config;
        }

        public Config getConfig() {

            return config;
        }

        public Connection getConnection() {

            return connection;
        }

        public String getCatalog() {

            return catalog;
        }

        public List<Table> getTables() {

            return tables;
        }
    }

    /**
     * The mysql table comment consumer.
     * @author Kahle
     */
    public static class MysqlTableCommentConsumer implements Consumer<Context> {
        @Override
        public void accept(Context context) {
            // Get parameters.
            Connection connection = context.getConnection();
            String catalog = context.getCatalog();
            List<Table> tables = context.getTables();
            // Build table name string.
            StringBuilder builder = new StringBuilder(); boolean first = true;
            for (Table table : tables) {
                if (first) { first = false; } else { builder.append(COMMA); }
                builder.append(SINGLE_QUOTE).append(table.getName()).append(SINGLE_QUOTE);
            }
            // Build the map of tables names tables objects.
            Map<String, Table> tableMap = new LinkedHashMap<String, Table>();
            for (Table table : tables) { tableMap.put(table.getName(), table); }
            // Declare sql and other variables.
            String sql = "SELECT `table_name`, `table_comment` FROM `information_schema`.`tables` " +
                    "WHERE `table_schema` = '" + catalog + "' AND `table_name` in (" + builder + ");";
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                // Execute the sql.
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    // Get the table name and table comment.
                    String tableComment = resultSet.getString(TWO);
                    String tableName = resultSet.getString(ONE);
                    // Fill the table comment.
                    Table table = tableMap.get(tableName);
                    if (table != null && StringUtils.isBlank(table.getComment())) {
                        table.setComment(tableComment);
                    }
                }
            }
            catch (Exception e) {
                throw ExceptionUtils.wrap(e);
            }
            finally {
                CloseUtils.closeQuietly(resultSet);
                CloseUtils.closeQuietly(statement);
            }
            // End.
        }
    }

}
