package artoria.jdbc;

import java.sql.SQLException;

/**
 * Db atom.
 * @author Kahle
 */
public interface DatabaseAtom {

    /**
     * Code run in atom.
     * @return Run success or failure
     * @throws SQLException Sql run error
     */
    boolean run() throws SQLException;

}
