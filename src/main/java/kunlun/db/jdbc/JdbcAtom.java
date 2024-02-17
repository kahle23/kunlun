/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc;

import java.sql.SQLException;

/**
 * Jdbc atom.
 * @author Kahle
 */
public interface JdbcAtom {

    /**
     * Code run in atom.
     * @return Run success or failure
     * @throws SQLException Sql run error
     */
    boolean run() throws SQLException;

}
