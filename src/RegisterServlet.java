import dao.ArticleDAO;
import dao.CommentDAO;
import dao.UserDAO;
import db.MySQLDatabase;
import dbObjects.User;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by glee156 on 23/01/2018.
 */
public class RegisterServlet extends HttpServlet {
    private HttpSession session;
    private MySQLDatabase db;
    private User newUser;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.db = new MySQLDatabase(getServletContext());
        this.session = req.getSession();

        //updates userInfo
        if (req.getParameter("profilePage") != null) {
            updateUserProfilePage(req, resp);
            return;
        }

        // get request parameters for userID and password
        if (session.getAttribute("username") == null) {

            // get reCAPTCHA request param
            String gRecaptchaResponse = req.getParameter("g-recaptcha-response");

            String ipAddress = req.getRemoteAddr();

            if (!verifyRecaptcha(gRecaptchaResponse, ipAddress)) {
                recaptchaFail(req, resp);
                return;
            }
        }

        Map<String, String[]> formParams = req.getParameterMap();

        //if user forgets password
        if (formParams.containsKey("resetPass")) {
            sendPasswordReset(formParams.get("resetPass")[0], req, resp);
            return;
        }

        makeNewUser(req, resp, formParams);

    }

    private void makeNewUser(HttpServletRequest req, HttpServletResponse resp, Map<String, String[]> formParams) throws ServletException, IOException {
        session = req.getSession();
        String username = formParams.get("username")[0];

        boolean admin = false;
        if (session.getAttribute("admin") != null) {
            admin = true;
        }
        //check if username is unique in database
        try (UserDAO userDAO = new UserDAO(db)) {
            //if not unique, redirect back
            if (!userDAO.isUnameUnique(username)) {
                if (admin) {
                    req.setAttribute("register", "fail");
                    req.getRequestDispatcher("admin.jsp").forward(req, resp);
                    return;
                }
                req.setAttribute("login", "fail");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //get parameters from form
        String firstname = formParams.get("firstname")[0];
        String lastname = formParams.get("lastname")[0];
        String email = formParams.get("email")[0];
        String dob = formParams.get("birthday")[0];
        String country = formParams.get("country")[0];
        String description = formParams.get("description")[0];
        String avatar = "images\\cookie_outline.png";
        String password = formParams.get("password")[0];


        if (req.getParameter("makeAdmin") == null) {
            newUser = new User(username, firstname, lastname, email, dob, country, description, avatar, password, false);
        } else {
            newUser = new User(username, firstname, lastname, email, dob, country, description, avatar, password, true);
        }

        try (UserDAO userDAO = new UserDAO(db)) {
            //checking if post request from registration form (ie. no previousUser) or from profile page save button
            userDAO.saveUser(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //after creating user, redirect
        chooseRedirect(req, resp, username, firstname, password);
        return;
    }

    private void chooseRedirect(HttpServletRequest req, HttpServletResponse resp, String username, String firstname, String password) throws ServletException, IOException {
        if (session.getAttribute("username") == null) {
            session.setAttribute("username", username);
            session.setAttribute("name", firstname);
            req.setAttribute("userID", username);
            req.setAttribute("userPassword", password);
            req.getRequestDispatcher("LoginServlet").forward(req, resp);
            return;

        } else {
            if (session.getAttribute("admin") != null) {
                req.getRequestDispatcher("admin.jsp").forward(req, resp);
                return;
            }
            session.setAttribute("name", firstname);
            req.getRequestDispatcher("profilePage.jsp").forward(req, resp);
        }
    }

    private void recaptchaFail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("recaptcha", "failed");
        if (session.getAttribute("admin") != null) {
            req.getRequestDispatcher("admin.jsp").forward(req, resp);
            return;
        }
        req.getRequestDispatcher("index.jsp").forward(req, resp);
        return;
    }

    private void updateUserProfilePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //getting all user info from profile page form
        Map<String, String[]> formParams = req.getParameterMap();

        String username = (String) session.getAttribute("username");
        String firstname = formParams.get("firstname")[0];
        String lastname = formParams.get("lastname")[0];
        String email = formParams.get("email")[0];
        String dob = formParams.get("birthday")[0];
        String country = formParams.get("country")[0];
        String description = formParams.get("description")[0];
        String password = formParams.get("password")[0];

        //getting admin and avatar info from user object
        User user = null;
        try (UserDAO userDAO = new UserDAO(db)) {
            user = userDAO.getUserByUname(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Boolean admin = user.getAdmin();
        String avatar = user.getAvatar();

        //new user object with all the updated details (except admin)
        User updateUser = new User(username, firstname, lastname, email, dob, country, description, avatar, password, admin);
        try (UserDAO userDAO = new UserDAO(db)) {
            userDAO.updateUser(updateUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //redirecting back to profile page
        req.getRequestDispatcher("profilePage.jsp").forward(req, resp);
    }

    //if user forgets password, send reset link to email address
    private void sendPasswordReset(String email, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = null;
        try (UserDAO userDAO = new UserDAO(db)) {
            user = userDAO.getUserByUname(email);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        //if email does not exist in database
        if (user == null) {
            req.setAttribute("reset", "fail");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }
        //timestamp expires 15 minutes after sending
        Timestamp date = new Timestamp(System.currentTimeMillis());
        long t = date.getTime();
        long m = 15 * 60 * 1000;
        date = new Timestamp(t + m);
        try (UserDAO userDAO = new UserDAO(db)) {
            userDAO.setExpiration(email, date);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        EmailAuthentication emailAuthentication = new EmailAuthentication();
        emailAuthentication.sendEmail(email);
        req.setAttribute("reset", "sent");
        req.getRequestDispatcher("index.jsp").forward(req, resp);

        return;

    }

    //link from password recovery email directs here
    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //email address is a parameter in link
        String email = req.getParameter("emailReset");
        User user = null;
        Timestamp expiration = null;

        try (UserDAO userDAO = new UserDAO(db)) {
            user = userDAO.getUserByUname(email);
            expiration = userDAO.checkExpiration(email);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        Timestamp current = new Timestamp(System.currentTimeMillis());
        //check if timestamp has expired
        if (current.after(expiration)) {
            req.setAttribute("reset", "fail");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("reset", "success");
        session.setAttribute("username", user.getUname());

        session.setAttribute("name", user.getFname());

        session.setAttribute("password", user.getPassword());

        req.getRequestDispatcher("profilePage.jsp").forward(req, resp);

        return;
    }


    //delete Account or reset password
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.db = new MySQLDatabase(getServletContext());
        session = req.getSession();

        //reset password
        if (req.getParameter("emailReset") != null) {
            resetPassword(req, resp);
            return;
        }


        String uname;

        //if deleting user from admin page
        if (session.getAttribute("admin") != null) {
            uname = req.getParameter("admin");
            //deleting this account
        } else {
            uname = (String) session.getAttribute("username");
        }
        //set articles and comments by user to anonymous
        setToAnonymous(uname);


        try (UserDAO userDAO = new UserDAO(db)) {

            userDAO.deleteUser(uname);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (session.getAttribute("admin") != null) {
            req.getRequestDispatcher("admin.jsp").forward(req, resp);
        } else {
            session.invalidate();
            User user = null;
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }

    private void setToAnonymous(String uname) {
        //set users articles and comments to anonymous
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            articleDAO.articleAuthorToAnonymous(uname);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (CommentDAO commentDAO = new CommentDAO(db)) {
            commentDAO.commentAuthorToAnonymous(uname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected boolean verifyRecaptcha(String response, String remoteip) {

        String secret = "6LdQB0MUAAAAAFmpmvY35C85z3IGNXAP2KfsCfDC";
        URLConnection connection = null;
        InputStream is = null;
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        String url = "https://www.google.com/recaptcha/api/siteverify";

        try {
            String query = String.format("secret=%s&response=%s&remoteip=%s",
                    URLEncoder.encode(secret, charset),
                    URLEncoder.encode(response, charset),
                    URLEncoder.encode(remoteip, charset));

            connection = new URL(url + "?" + query).openConnection();
            is = connection.getInputStream();

            Reader in = new InputStreamReader(is);
            Object json = JSONValue.parse(in);

            JSONObject obj = (JSONObject) json;
            if ((boolean) obj.get("success") == true) {
                return true;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }

            }
        }

        return false;
    }


}
