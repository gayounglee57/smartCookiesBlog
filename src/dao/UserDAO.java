package dao;

import db.Database;
import dbObjects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An object in charge of loading / saving {@link User} objects to / from a database.
 */
public class UserDAO implements AutoCloseable {

    private final Connection conn;

    /**
     * Creates a new DAO with a {@link Connection} from the given {@link Database}.
     *
     * @param db the {@link Database} from which to establish a {@link Connection}
     * @throws SQLException if something went wrong.
     */
    public UserDAO(Database db) throws SQLException {
        this.conn = db.getConnection();
    }

    /**
     * Gets a list of {@link User}s from the database.
     *
     * @return a {@link List} of {@link User} objects
     * @throws SQLException if something went wrong.
     */
    public List<User> getAllUsers() throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cookies_users")) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<User> Users = new ArrayList<>();
                while (rs.next()) {
                    Users.add(UserFromResultSet(rs));
                }
                return Users;
            }
        }
    }

    /**
     * Gets the {@link User} with the given id from the database.
     *
     * @param uName the id to check
     * @return the {@link User} with the given id, or <code>null</code> if none exists.
     * @throws SQLException if something went wrong.
     */
    public User getUserByUname(String uName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cookies_users WHERE uname = ? or email = ?")) {
            stmt.setString(1, uName);
            stmt.setString(2, uName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return UserFromResultSet(rs);
                } else {
                    return null;
                }
            }
        }
    }
    public void setExpiration (String uname, Timestamp date) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE cookies_users SET expiration = ? WHERE email = ?")) {
            stmt.setTimestamp(1, date);
            stmt.setString(2, uname);
            stmt.executeUpdate();
        }
    }
    public Timestamp checkExpiration (String uname) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT expiration FROM cookies_users WHERE email = ?")) {
            stmt.setString(1, uname);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp(1);
                } else {
                    return null;
                }
            }
        }
    }


    /**
     * Creates an {@link User} from a {@link ResultSet} at its current cursor location.
     *
     * @param rs the {@link ResultSet}
     * @return the {@link User}
     * @throws SQLException if something went wrong.
     */
    private User UserFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),rs.getString(9));
    }

    /**
     * Saves the given {@link User} to the database.
     * <p>If the User's {@link User#uname} is <code>null</code>, a new
     * row will be added to the database and the given User's id will be set to the value generated by the
     * database.</p>
     * <p>If the User's id is already set, the row for the User with that id will be updated instead.</p>
     *
     * @param user the User to save
     * @throws SQLException if something went wrong.
     */
    public void saveUser(User user) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cookies_users WHERE uname = ?")) {
            stmt.setString(1, user.getUname());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    addUser(user);
                } else {
                    updateUser(user);
                }
            }
        }
    }




    public boolean isUnameUnique (String uname) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cookies_users WHERE uname = ?")) {
            stmt.setString(1, uname);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return true;
                }
            }
        }
            return false;
        }



    /**
     * Adds the given {@link User} to the database.
     * <p>If the User's id is <code>null</code>, its id will be auto-generated by the database and set back to
     * the given {@link User} object. Otherwise, the given id will be used.</p>
     *
     * @param user the User to add
     * @throws SQLException if something went wrong.
     */
    public void addUser(User user) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO cookies_users VALUES (?,?,?,?,?,?,?,?,?,?,?)")) {
            stmt.setString(1, user.getUname());
            stmt.setString(2, user.getFname());
            stmt.setString(3, user.getLname());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getDob());
            stmt.setString(6, user.getCountry());
            stmt.setString(7, user.getDescription());
            stmt.setString(8, user.getAvatar());
            stmt.setString(9, user.getPassword());
            stmt.setBoolean(10, user.getAdmin());
            stmt.setTimestamp(11, null);
            stmt.executeUpdate();
        }
    }





    /**
     * Updates the given {@link User}'s entry in the database.
     *
     * @param User the User to update
     * @throws SQLException if something went wrong.
     */
    public void updateUser(User User) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE cookies_users SET fname = ?,lname = ?, email = ?," +
                "dob = ?,country = ? ,description = ?,avatar = ?,pwd = ? WHERE uname = ?")) {
            stmt.setString(1, User.getFname());
            stmt.setString(2, User.getLname());
            stmt.setString(3, User.getEmail());
            stmt.setString(4, User.getDob());
            stmt.setString(5, User.getCountry());
            stmt.setString(6, User.getDescription());
            stmt.setString(7, User.getAvatar());
            stmt.setString(8,User.getPassword());
            stmt.setString(9, User.getUname());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes the User with the given id from the database, if any.
     *
     * @param uname the id to check
     * @throws SQLException if something went wrong.
     */
    public void deleteUser(String uname) throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM cookies_users WHERE uname = ?")) {
            stmt.setString(1, uname);
            stmt.executeUpdate();
        }
    }



    public boolean isAdmin (String uname) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT uname FROM cookies_users WHERE uname = ? and admin = 1")) {
            stmt.setString(1, uname);
            try (ResultSet rs = stmt.executeQuery()) {
               while(rs.next()) {
                   return true;
               }
            } catch (SQLException e){
                return false;
            }
        }
       return false;
    }


    /**
     * Checks if user/password matches
     * @param unameOrEmail the username to check
     *           @param password to check
     *
     */
    public boolean validateUser(String unameOrEmail, String password){
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cookies_users WHERE uname = ? and pwd = ?")) {
            stmt.setString(1, unameOrEmail);
            stmt.setString(2,password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
                e.printStackTrace();
            }
        return false;
    }

    /**
     * Returns true if the path to avatar has been added to database with correct user; returns false otherwise
     *
     */
    public boolean setAvatarPath(String uname, String avatarPath){
        boolean toReturn = false;
        try(PreparedStatement stmt = conn.prepareStatement("UPDATE cookies_users SET avatar = ? WHERE uname = ?")) {
            stmt.setString(1, avatarPath);
            stmt.setString(2, uname);
            stmt.executeUpdate();
            toReturn = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * Closes the {@link Connection} that was created when this DAO was created.
     *
     * @throws SQLException if something went wrong.
     */
    @Override
    public void close() throws SQLException {
        this.conn.close();
    }
}
