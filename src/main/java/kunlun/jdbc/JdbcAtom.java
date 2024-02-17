package kunlun.jdbc;

import java.sql.SQLException;

/**
 * Jdbc atom.
 * @author Kahle
 */
@Deprecated
public interface JdbcAtom {

    /**
     * Code run in atom.
     * @return Run success or failure
     * @throws SQLException Sql run error
     */
    boolean run() throws SQLException;

}
