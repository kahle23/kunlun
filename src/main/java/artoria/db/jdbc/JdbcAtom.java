package artoria.db.jdbc;

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
