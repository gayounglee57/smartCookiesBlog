import dao.ArticleDAO;
import dao.CommentDAO;
import dao.UserDAO;
import db.MySQLDatabase;
import dbObjects.Article;
import dbObjects.Comment;
import dbObjects.User;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by mmIZ318 on 30/01/2018.
 */
public class AdminServlet extends HttpServlet {
    MySQLDatabase db;


    //loads user info in admin page with AJAX
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    this.db = new MySQLDatabase(getServletContext());
        List<User> userList = null;
        try (UserDAO userDAO = new UserDAO(db)) {
            userList = userDAO.getAllUsers();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        JSONObject obj = new JSONObject();
            for (int i= 0; i < userList.size(); i++){
                obj.put(i, userList.get(i).getUname());

            }

        resp.getWriter().write(obj.toJSONString());
    }

    //method for when admin selects a user or article to get more details
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.db = new MySQLDatabase(getServletContext());

        if (req.getParameter("className").contains("user")){
            try {
                getUser(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                getArticle(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //get Article details AJAX
    private void getArticle(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        Article article;
        try (ArticleDAO articleDAO = new ArticleDAO(db)){
            article = articleDAO.getArticleById(req.getParameter("id"));
        }
        String title = article.getTitle();
        String description = article.getDescription();
        String content = article.getContent();
        String author = article.getUserName();
        String articleID = Integer.toString(article.getId());

        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("description", description);
        obj.put("content", content);
        obj.put("author", author);
        obj.put("articleID", articleID);

        List<Comment> list;
        //get comments for the article
        try(CommentDAO commentDAO = new CommentDAO(db)) {
            list = commentDAO.getCommentsForAdmin(req.getParameter("id"));
        }
        for (int i = 0; i < list.size(); i++) {
            JSONObject comment = new JSONObject();
            comment.put("commentContent", list.get(i).getComment());
            comment.put("commentAuthor", list.get(i).getUserName());
            comment.put("commentID", list.get(i).getId());
            obj.put(i, comment);
        }

        resp.getWriter().write(obj.toJSONString());
    }
    //get user details AJAX
    private void getUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        User user;
        try (UserDAO userDAO = new UserDAO(db)){
            user = userDAO.getUserByUname(req.getParameter("id"));
        }
        String username = user.getUname();
        String fname = user.getFname();
        String lname = user.getLname();

        JSONObject obj = new JSONObject();
        obj.put("username", username);
        obj.put("fname", fname);
        obj.put("lname", lname);
        //get articles by this user
        List<Article> list;
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            list = articleDAO.getMyArticles(user);
        }
        for (int i = 0; i < list.size(); i++) {
            JSONObject article = new JSONObject();
            article.put("article", list.get(i).getTitle());
            article.put("articleID", list.get(i).getId());
            obj.put(i, article);
        }
        resp.getWriter().write(obj.toJSONString());
    }
}
