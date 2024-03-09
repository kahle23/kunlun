/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.support;

import kunlun.core.Loader;
import kunlun.db.jdbc.meta.Column;
import kunlun.db.jdbc.meta.Table;
import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;
import kunlun.util.CloseUtils;
import kunlun.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.COMMA;
import static kunlun.util.CollectionUtils.isNotEmpty;

public class JdbcTableLoader implements Loader<JdbcTableLoader.Config, List<Table>> {

    @Override
    public List<Table> load(Config config) {
        // Get parameters and validate.
        DataSource dataSource = config.getDataSource();
        String url      = config.getUrl();
        String username = config.getUsername();
        String password = config.getPassword();
        String catalog  = config.getCatalog();
        if (dataSource == null) {
            Assert.notBlank(url     , "Parameter \"url\" must not blank. ");
            Assert.notBlank(username, "Parameter \"username\" must not blank. ");
            Assert.notNull(password , "Parameter \"password\" must not null. ");
        }
        // Do load.
        Connection connection = null;
        try {
            connection = dataSource != null
                    ? dataSource.getConnection() : DriverManager.getConnection(url, username, password);
            Collection<String> reservedTables = config.getReservedTables();
            Collection<String> excludedTables = config.getExcludedTables();
            return loadTables(connection, catalog, reservedTables, excludedTables);
        }
        catch (Exception e) { throw ExceptionUtils.wrap(e); }
        finally { CloseUtils.closeQuietly(connection); }
    }

    public List<Table> loadTables(Connection connection, String catalog
            , Collection<String> reservedTables, Collection<String> excludedTables) throws SQLException {
        // Validate and declare.
        Assert.notNull(connection, "Parameter \"connection\" must not null. ");
        List<Table> tables = new ArrayList<Table>();
        String[] types = new String[]{"TABLE"};
        ResultSet tableRs = null;
        // Do load tables.
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            tableRs = dbMetaData.getTables(catalog, (String) null, (String) null, types);
            while (tableRs.next()) {
                String tableName = tableRs.getString((String) "TABLE_NAME");
                String remarks = tableRs.getString((String) "REMARKS");
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
        finally {
            CloseUtils.closeQuietly(tableRs);
            CloseUtils.closeQuietly(connection);
        }
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
            columnRs = dbMetaData.getColumns(catalog, null, tableName, null);
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
            pkResultSet = dbMetaData.getPrimaryKeys(catalog, null, table);
            while (pkResultSet.next()) {
                pkBuilder.append(pkResultSet.getString("COLUMN_NAME")).append(COMMA);
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
        String columnName = columnRs.getString("COLUMN_NAME");
        // "IS_NULLABLE" is "NO" or "YES".
        String isNullable = columnRs.getString("IS_NULLABLE");
        // "IS_AUTOINCREMENT" is "NO" or "YES".
        String isAutoincrement = columnRs.getString("IS_AUTOINCREMENT");
        // Judge nullable and autoincrement.
        Boolean nullable = null, autoincrement = null;
        if (StringUtils.isNotBlank(isNullable)) {
            nullable = "YES".equalsIgnoreCase(isNullable);
        }
        if (StringUtils.isNotBlank(isAutoincrement)) {
            autoincrement = "YES".equalsIgnoreCase(isAutoincrement);
        }
        // Create column object.
        Column column = new Column();
        column.setName(columnName);
        //column.setOrder();
        column.setType(columnRs.getString("TYPE_NAME"));
        column.setSize(columnRs.getInt("COLUMN_SIZE"));
        column.setDecimalDigits(columnRs.getInt("DECIMAL_DIGITS"));
        column.setNullable(nullable);
        //column.setDefaultValue();
        column.setComment(columnRs.getString("REMARKS"));
        column.setPrimaryKey(primaryKeys.contains(columnName));
        column.setAutoincrement(autoincrement);
        return column;
    }

    public static class Config {
        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private String catalog;
        private DataSource dataSource;
        private Collection<String> reservedTables;
        private Collection<String> excludedTables;

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

        public void setReservedTables(Collection<String> reservedTables) {

            this.reservedTables = reservedTables;
        }

        public Collection<String> getExcludedTables() {

            return excludedTables;
        }

        public void setExcludedTables(Collection<String> excludedTables) {

            this.excludedTables = excludedTables;
        }
    }

}
