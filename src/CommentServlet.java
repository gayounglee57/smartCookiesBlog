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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by glee156 on 23/01/2018.
 */
public class CommentServlet extends HttpServlet {
    private MySQLDatabase db;
    private User user;
    private Article article;
    private HttpSession session;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        this.db = new MySQLDatabase(getServletContext());
        this.session = req.getSession();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        //This handles display comments only
        if (req.getParameter("articleloading") != null) {
            displayComments(resp);
            return;
        //This handles hide/show
        } else if (req.getParameter("hide") != null) {
            try {
                showOrHideComment(req, resp);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //This block handles deleting comments
        } else if (req.getParameter("deleteComment") != null) {
            deleteComment(req, resp);
            return;
            //This block handles updating commments
        } else if (req.getParameter("saveChanges") != null) {
            updateComment(req,resp);
            return;
        }
        //This block handles displaying comments
        else {
            //Display comments on article page ie. send a response back to ajax call from commentsAjax.js
            commentAjaxResponse(req, resp, currentTime);
        }
    }

    private void commentAjaxResponse(HttpServletRequest req, HttpServletResponse resp, Timestamp currentTime) throws IOException {
        int articleid = Integer.parseInt((String)session.getAttribute("articleid"));

        String username = (String) session.getAttribute("username");

        String parentIdString;
        int parentid = 0;
        if ((parentIdString = req.getParameter("parentid")) != null) {

            parentid = Integer.parseInt(parentIdString);
        }
        int childid = 0;

        String comment = req.getParameter("comment");

        //this try block handles adding a new comment
        Comment commentObject = new Comment(articleid, username, comment, currentTime, parentid, childid);
        try (CommentDAO commentDAO = new CommentDAO(db)) {
            commentDAO.addComment(commentObject);
            JSONObject newComm = new JSONObject();

            try (UserDAO userDAO = new UserDAO(db)){
                User user = userDAO.getUserByUname(username);
                newComm.put("Avatar",user.getAvatar());
            }
            newComm.put("Date", "" + currentTime);
            newComm.put("CommentId","" + commentDAO.getMaxID());
            newComm.put("User", username);
            String object = newComm.toJSONString();
            resp.getWriter().write(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void showOrHideComment(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Boolean show = true;
        if (req.getParameter("hide").equals("false")) {
            show = false;
        }
        try (CommentDAO commentDAO = new CommentDAO(db)) {
            commentDAO.showComment(id, show);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("admin.jsp").forward(req, resp);
    }


    private void deleteComment(HttpServletRequest req, HttpServletResponse resp) throws IOException{

        //this block handles deleting a comment
        String idString = req.getParameter("commentId");
        int id = Integer.parseInt(idString);
        try (CommentDAO commentDAO = new CommentDAO(db)) {
            commentDAO.deleteComment(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject response = new JSONObject();
        response.put("deletedcomment",true);
        resp.getWriter().write(response.toJSONString());
    }

    private void updateComment(HttpServletRequest req, HttpServletResponse resp) throws IOException{

                //getting params from request to create new Comment object for DAO
                int id = Integer.parseInt(req.getParameter("commentId"));
                String commentNew = req.getParameter("comment");

                try (CommentDAO commentDAO = new CommentDAO(db)) {
                    commentDAO.updateComment(id, commentNew);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

    }

    private void displayComments(HttpServletResponse resp) throws IOException {

        List<Comment> comments = null;
        try (CommentDAO commentDAO = new CommentDAO(db)) {
            comments = commentDAO.getCommentsByArticleId((String)session.getAttribute("articleid"));
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

        String author = "";
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            article = articleDAO.getArticleById((String)session.getAttribute("articleid"));
            author = article.getUserName();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject groupOfObjects = new JSONObject();
        groupOfObjects.put("Author", author);
        groupOfObjects.put("userLoggedIn",session.getAttribute("username"));
        createJSONObj(comments, groupOfObjects);

        if (groupOfObjects.size() != 0) {
            String object = groupOfObjects.toJSONString();
            resp.getWriter().write(object);
            return;
        } else {
            JSONObject response = new JSONObject();
            response.put("nocomments",true);
            resp.getWriter().write(response.toJSONString());
            return;
        }

    }

    private void createJSONObj(List<Comment> comments, JSONObject groupOfObjects) {
        int i = 0;

        for (Comment c : comments) {
            //get all saved info on the user to get avatar info
            try (UserDAO userDAO = new UserDAO(db)) {
                if ((user = userDAO.getUserByUname(c.getUserName())) == null){
                    continue;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JSONObject obj = new JSONObject();
            obj.put("Avatar", user.getAvatar());
            obj.put("Username", c.getUserName());
            obj.put("CommentId", c.getId());
            obj.put("Comment", c.getComment());;
            obj.put("Date", c.getDateTime().toString());
            obj.put("Parent",c.getParentId());
            obj.put("Child",c.getChildId());
            groupOfObjects.put(i, obj);
            i++;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
