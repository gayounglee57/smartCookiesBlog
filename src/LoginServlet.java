


/**
 * Created by tpre939 on 22/01/2018.
 */

import dao.UserDAO;
import db.MySQLDatabase;
import dbObjects.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private HttpSession session;
    private User user;
    private MySQLDatabase db;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.db = new MySQLDatabase(getServletContext());
        session = req.getSession();
        Map<String, String[]> map = req.getParameterMap();

        if (req.getParameter("logout") != null) {
            logout(req, resp);

            //Check if already logged in
        } else {
            String username = null;
            String userPassword = null;
            //Returns false if no username or email/password combination results in the db
            boolean valid = false;

            if (req.getParameter("cookie") == null) {
                username = map.get("username")[0];

                userPassword = map.get("password")[0];

                try (UserDAO userDAO = new UserDAO(db)) {
                    valid = userDAO.validateUser(username, userPassword);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //login with remember me functionality
            } else {
                valid = true;
                Cookie[] c = req.getCookies();

                for (Cookie cookie : c) {
                    if (cookie.getName().equals("username")) {
                        username = cookie.getValue();
                        break;
                    }
                }
            }
            if (valid) {

                logIn(req, resp, username, userPassword);


            } else {
                req.setAttribute("loginFailed", "true");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        }
    }

    private void logIn(HttpServletRequest req, HttpServletResponse resp, String username, String userPassword) throws ServletException, IOException {
        try (UserDAO userDAO = new UserDAO(db)) {
            user = userDAO.getUserByUname(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("username", username);
        session.setAttribute("name", user.getFname());
        session.setAttribute("password", userPassword);
        session.setAttribute("source", "index");

        //Sets the session to never expire and creates cookie
//        if (req.getParameter("remember") != null) {
//            Cookie c = rememberMe(username);
//            resp.addCookie(c);
//        }

        try {
            redirectToPage(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //chooses which page to redirect to
    private void redirectToPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        boolean isAdmin = false;
        try (UserDAO userDAO = new UserDAO(db)) {
            isAdmin = userDAO.isAdmin(user.getUname());
        }
        //redirect to ADMIN PAGE
        if (isAdmin) {
            session.setAttribute("admin", "admin");
            req.getRequestDispatcher("admin.jsp").forward(req, resp);
            //redirect to article page
        } else if (req.getParameter("redirectId") != null) {
            String article = req.getParameter("redirectId");
            req.getRequestDispatcher("ArticleServlet?articleid=" + article + "&userLoggedIn=" + user.getUname() + "&Author=" + session.getAttribute("Author")).forward(req, resp);
            //redirect to home page
        } else {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }

    private Cookie rememberMe(String username) {
        Cookie c = new Cookie("username", username);
        session.setMaxInactiveInterval(-1);
        return c;

    }
    //log out - remove remember me cookie if exists
    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] c = req.getCookies();
        for (Cookie cookie : c) {
            if (cookie.getName().equals("username")) {
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
                break;
            }
        }
        session.removeAttribute("name");
        session.removeAttribute("username");
        session.invalidate();
        req.setAttribute("cookie",null);
        resp.sendRedirect("index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}