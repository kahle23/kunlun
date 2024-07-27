/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.support.function;

import kunlun.core.function.Consumer;
import kunlun.db.jdbc.meta.Table;
import kunlun.db.jdbc.support.JdbcTableLoader;
import kunlun.exception.ExceptionUtils;
import kunlun.util.CloseUtils;
import kunlun.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.TWO;
import static kunlun.common.constant.Symbols.COMMA;
import static kunlun.common.constant.Symbols.SINGLE_QUOTE;

/**
 * The mysql table comment consumer.
 * @author Kahle
 */
public class MysqlTableCommentConsumer implements Consumer<JdbcTableLoader.Context> {

    @Override
    public void accept(JdbcTableLoader.Context context) {
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
