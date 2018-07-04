package dao;

import db.Database;
import dbObjects.Article;
import dbObjects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An object in charge of loading / saving {@link Article} objects to / from a database.
 */
public class ArticleDAO implements AutoCloseable {
    private final Connection conn;

    /**
     * Creates a new DAO with a {@link Connection} from the given {@link Database}.
     *
     * @param db the {@link Database} from which to establish a {@link Connection}
     * @throws SQLException if something went wrong.
     */
    public ArticleDAO(Database db) throws SQLException {
        this.conn = db.getConnection();
    }

    /**
     * Gets a list of {@link Article}s from the database.
     *
     * @return a {@link List} of {@link Article} objects
     * @throws SQLException if something went wrong.
     */
    public int countArticles() throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cookies_articles")) {
            try (ResultSet rs = stmt.executeQuery()) {
                int i = 0;
                while (rs.next()) {
                    i++;
                }
                return i;
            }
        }
    }

    public List<Article> getAllArticles () throws SQLException {
        List<Article> articles = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cookies_articles ORDER BY date DESC")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    articles.add(articleFromResultSet(rs));
                }
            }
        }
        return articles;
    }


    public synchronized List<Article> getNextArticles(int index,int noOfArticles,String sortBy, String ascending) throws SQLException {
        String sqlStatement = "";
        switch (sortBy) {
            case "articleID":
                if (ascending.equals("DESC")) {
                    sqlStatement = "SELECT * FROM cookies_articles WHERE date < ? AND hide != 1 order by articleID DESC LIMIT ?,?";
                } else sqlStatement = "SELECT * FROM cookies_articles WHERE date < ? AND hide != 1 order by articleID ASC LIMIT ?,?";
                return executeQuery(sqlStatement,index,noOfArticles);

            case "uname":
                if (ascending.equals("DESC")){
                    sqlStatement = "SELECT * FROM cookies_articles WHERE date < ? AND hide != 1 order by uname DESC LIMIT ?,?";
                } else sqlStatement = "SELECT * FROM cookies_articles WHERE date < ? AND hide != 1 order by uname ASC LIMIT ?,?";
                return executeQuery(sqlStatement,index,noOfArticles);

            case "title":
                if (ascending.equals("DESC")){
                    sqlStatement = "SELECT * FROM cookies_articles WHERE date < ? AND hide != 1 order by title DESC LIMIT ?,?";
                } else sqlStatement = "SELECT * FROM cookies_articles WHERE date < ? AND hide != 1 order by title ASC LIMIT ?,?";
                return executeQuery(sqlStatement,index,noOfArticles);
        }
        return null;


    }

     private List<Article> executeQuery(String sqlStatement,int index,int noOfArticles) throws SQLException{
         Timestamp now = new Timestamp(System.currentTimeMillis());
         List<Article> articles = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sqlStatement)) {
             stmt.setTimestamp(1, now);
             stmt.setInt(2, index);
             stmt.setInt(3, noOfArticles);
             try (ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                     articles.add(articleFromResultSet(rs));
                 }
                 return articles;
             }
         }
     }



    /**
     * Gets the list of Articles with the given user from the database.
     *
     * @param user the User to retrieve articles for
     * @return a list of articles with the given user, or <code>null</code> if none exists.
     * @throws SQLException if something went wrong.
     */
    public List<Article> getMyArticles(User user) throws SQLException {
        List<Article> articles = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cookies_articles WHERE uname=? AND hide != 1")) {
            stmt.setString(1, user.getUname());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    articles.add(articleFromResultSet(rs));
                }
                return articles;
            }
        }
    }

    public void showComment (int id, Boolean show) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE cookies_articles SET hide = ? WHERE articleID = ?")) {
            stmt.setBoolean(1, show);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }



    /**
     * Gets the {@link Article} with the given id from the database.
     *
     * @param id the id to check
     * @return the {@link Article} with the given id, or <code>null</code> if none exists.
     * @throws SQLException if something went wrong.
     */
    public Article getArticleById(String id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cookies_articles WHERE articleID=?")) {
            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return articleFromResultSet(rs);
                } else {
                    return null;
                }
            }
        }
    }


    /**
     * Gets the {@link Article} with the given id from the database.
     *
     * @param search the id to check
     * @return the {@link List<Article>} with the given title, or <code>null</code> if none exists.
     * @throws SQLException if something went wrong.
     */
    public synchronized List<Article> getArticlesBySearch(String search,String sortBy, String ascending) throws SQLException {
        String sqlStatement = "";
        switch (sortBy) {
            case "articleID":
                if (ascending.equals("DESC")) {
                    sqlStatement = "SELECT * FROM cookies_articles WHERE title LIKE ? AND hide != 1 AND date < ? order by articleID DESC";
                } else
                    sqlStatement = "SELECT * FROM cookies_articles WHERE title LIKE ? AND hide != 1 AND date < ? order by articleID ASC";
                return executeSearchQuery(sqlStatement, search);

            case "uname":
                if (ascending.equals("DESC")) {
                    sqlStatement = "SELECT * FROM cookies_articles WHERE title LIKE ? AND hide != 1 AND date < ? order by uname DESC";
                } else
                    sqlStatement = "SELECT * FROM cookies_articles WHERE title LIKE ? AND hide != 1 AND date < ? order by uname ASC";
                return executeSearchQuery(sqlStatement, search);

            case "title":
                if (ascending.equals("DESC")) {
                    sqlStatement = "SELECT * FROM cookies_articles WHERE title LIKE ? AND hide != 1 AND date < ? order by title DESC";
                } else
                    sqlStatement = "SELECT * FROM cookies_articles WHERE title LIKE ? AND hide != 1 AND date < ? order by title ASC";
                return executeSearchQuery(sqlStatement, search);


        }
        return null;
    }

    private List<Article> executeSearchQuery(String sqlStatement,String search) throws SQLException{
                List<Article> articles = new ArrayList<>();
                Timestamp now = new Timestamp(System.currentTimeMillis());
            try (PreparedStatement stmt = conn.prepareStatement(sqlStatement)) {
                stmt.setString(1, "%" + search + "%");
                stmt.setTimestamp(2,now);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        articles.add(articleFromResultSet(rs));
                    }
                    return articles;
                }
            }
        }


    /**
     * Creates an {@link Article} from a {@link ResultSet} at its current cursor location.
     *
     * @param rs the {@link ResultSet}
     * @return the {@link Article}
     * @throws SQLException if something went wrong.
     */
    private Article articleFromResultSet(ResultSet rs) throws SQLException {
        return new Article(rs.getInt(1),rs.getString(2), rs.getTimestamp(3), rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7), rs.getInt(8), rs.getString(9));
    }

    /**
     * Saves the given {@link Article} to the database.
     * <p>If the article's {@link Article#id} is <code>null</code>, a new
     * row will be added to the database and the given article's id will be set to the value generated by the
     * database.</p>
     * <p>If the article's id is already set, the row for the article with that id will be updated instead.</p>
     *
     * @param article the article to save
     * @throws SQLException if something went wrong.
     */
    public void saveArticle(Article article) throws SQLException {
        if (article.getId() == null) {

            addArticle(article);
        } else {

            updateArticle(article);
        }
    }

    /**
     * Adds the given {@link Article} to the database.
     * <p>If the article's id is <code>null</code>, its id will be auto-generated by the database and set back to
     * the given {@link Article} object. Otherwise, the given id will be used.</p>
     *
     * @param article the article to add
     * @throws SQLException if something went wrong.
     */
    public void addArticle(Article article) throws SQLException {
        if (article.getId() == null) {
            addArticle_generateId(article);
        } else {
            addArticle_supplyId(article);
        }
    }

    /**
     * Adds the given {@link Article} to the database.
     * <p>Ignores any id value supplied, and uses the database to generate one. Then, sets the id of the given article
     * to the generated value.</p>
     *
     * @param article the article to add
     * @throws SQLException if something went wrong.
     */
    private void addArticle_generateId(Article article) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO cookies_articles (uname,date,title,description,content,images,hide) VALUES (?,?,?,?,?,?,?)")) {
            stmt.setString(1, article.getUserName());
            stmt.setTimestamp(2, article.getDate());
            stmt.setString(3,article.getTitle());
            stmt.setString(4,article.getDescription());
            stmt.setString(5, article.getContent());
            stmt.setString(6, article.getImages());
            stmt.setInt(7, article.getHide());
            stmt.executeUpdate();
            try (PreparedStatement newstmt = conn.prepareStatement("SELECT max(articleID) FROM cookies_articles")) {
                try (ResultSet rs = newstmt.executeQuery()) {
                    if (rs.next()) {
                        article.setId(rs.getInt(1));
                    }
                }
            }

            }
    }


    /**
     * Adds the given {@link Article} to the database.
     * <p>Uses the article's id (which must not be <code>null</code>).</p>
     *
     * @param article the article to add
     * @throws IllegalArgumentException if the given article's id is null.
     * @throws SQLException             if something went wrong.
     */
    private void addArticle_supplyId(Article article) throws SQLException {

        if (article.getId() == null) {
            throw new IllegalArgumentException("article's id cannot be null.");
        }

        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO cookies_articles (articleID, userID, date,content) VALUES (?, ?, ?,?)")) {
            stmt.setInt(1, article.getId());
            stmt.setString(2, article.getUserName());
            stmt.setTimestamp(3, article.getDate());
            stmt.setString(4, article.getContent());
            stmt.executeUpdate();
        }
    }

    /**
     * Updates the given {@link Article}'s entry in the database.
     *
     * @param article the article to update
     * @throws SQLException if something went wrong.
     */
    public void updateArticle(Article article) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE cookies_articles SET title = ?, description = ?, date = ?, content = ? WHERE articleID = ?")) {
            stmt.setString(1, article.getTitle());
            stmt.setString(2, article.getDescription());
            stmt.setTimestamp(3, article.getDate());
            stmt.setString(4,article.getContent());
            stmt.setInt(5,article.getId());
            stmt.executeUpdate();
        }
    }


    /**
     * When the user is deleted set the author of any of their articles to anonymous
     *
     * @param uname the username to check
     * @throws SQLException if something went wrong.
     */
    public void articleAuthorToAnonymous (String uname) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE cookies_articles SET uname = 'anonymous' WHERE uname = ?")) {
            stmt.setString(1, uname);
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes the article with the given id from the database, if any.
     *
     * @param id the id to check
     * @throws SQLException if something went wrong.
     */
    public void deleteArticle(int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM cookies_articles WHERE articleID = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }




    /**
     * Adding image path to database with article it was uploaded from
     *
     */
    public void addImages(int articleID, String imagePaths) throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement("UPDATE cookies_articles SET images = ? WHERE articleID = ?")) {
            stmt.setString(1, imagePaths);
            stmt.setInt(2, articleID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adding youtube link to database with article it was uploaded from
     *
     */
    public void addYoutubeLink(String youtubeLink, int articleID){
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE cookies_articles SET youtube = ? WHERE articleID = ?")) {
            stmt.setString(1, youtubeLink);
            stmt.setInt(2, articleID);
            stmt.executeUpdate();
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
