package db;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A MySQL database connection.
 */
public class MySQLDatabase implements Database {

    private final Properties props;

    public MySQLDatabase(ServletContext context) throws IOException {

        // Forces the MySQL driver to load properly. There's nothing much we can do if this fails,
        // so I'm just converting it to an unchecked exception.
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        props = new Properties();
        try (InputStream in = context.getResourceAsStream("/WEB-INF/mysql-connection.properties")) {
            props.load(in);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(props.getProperty("url"), props);
    }
}