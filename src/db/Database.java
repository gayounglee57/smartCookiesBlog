package db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Implementing classes can provide connections to a database.
 */
public interface Database {

    Connection getConnection() throws SQLException;

}