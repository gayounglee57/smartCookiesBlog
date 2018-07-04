import dao.ArticleDAO;
import dao.UserDAO;
import db.MySQLDatabase;
import dbObjects.Article;
import dbObjects.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tpre939 on 24/01/2018.
 */
public class ArticleServlet extends HttpServlet {

    private int articleIndex;
    private MySQLDatabase db;
    private HttpSession session;
    private User user;

//all things article related
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        this.db = new MySQLDatabase(getServletContext());
        this.session = req.getSession();

        //set article to hide/show from admin page
        if (req.getParameter("hide") != null) {
            try {
                showOrHideArticle(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //This block handles saving changes to articles
        else if (req.getParameter("writingArticle") != null) {

            String username = (String) session.getAttribute("username");
            String title = req.getParameter("title");
            String description = req.getParameter("recipedescrip");
            String content = req.getParameter("articleContent");
            String imagePaths = null;
            Timestamp date;
            if (req.getParameter("dateandtime") == null || req.getParameter("dateandtime") .equals("")) {
                date = new Timestamp(System.currentTimeMillis());
            } else {
                date = Timestamp.valueOf(req.getParameter("dateandtime"));
            }
            addNewArticle(req, resp, username, title, description, content, imagePaths,date);

        }
        //This block handles responding to ajax call for images from article.jsp
        else if (req.getParameter("images") != null) {

            imagesAjaxResponse(req, resp);
            return;
        }

        //enter this block to handle redirect thumbnail link to article page or to redirect back to same article
        else if (req.getParameter("articleid") != null) {
            session.setAttribute("articleid", req.getParameter("articleid"));
            thumbnailToArticle(req, resp);
            return;
            //This block handles adding new articles

        //deletes article from article.jsp
        } else if (req.getParameter("delete") != null) {
            try {
                deleteArticles(req, resp);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //This block handles creating new article
        else if (ServletFileUpload.isMultipartContent(req)) {

            try {
                //handling media only that came from article.jsp or media AND article that came from new article modal on index.jsp or profilePage.jsp
                saveImagesAndArticles(req, resp);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //This block handles displaying the thumbnails
        else {
            try {
                loadArticles(req, resp);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    //show or hide from admin page
    private void showOrHideArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(req.getParameter("id"));
        Boolean show = true;
        if (req.getParameter("hide").equals("false")) {
            show = false;
        }
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            articleDAO.showComment(id, show);
            req.getRequestDispatcher("admin.jsp").forward(req, resp);
            return;
        }
    }
    //delete article from article.jsp, redirect to homepage
    private void deleteArticles(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            articleDAO.deleteArticle(Integer.parseInt(req.getParameter("delete")));
        }
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            articleDAO.deleteArticle(Integer.parseInt(req.getParameter("delete")));
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
    //AJAX call for loading articles on homepage and profile page
    private void loadArticles(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        //This sets the DAO to retrieve the items from the top of the result set
        if ((req.getParameter("isPageReloaded") != null) && (req.getParameter("isPageReloaded").equals("reload"))) {
            articleIndex = 0;
        }
        //This retrieves the number of articles to load from the ajaxCall function in modalAction
        int noOfArticles;
        if (session.getAttribute("admin") != null) {
            try (ArticleDAO articleDAO = new ArticleDAO(db)) {
                noOfArticles = articleDAO.countArticles();
            }
        } else {
            noOfArticles = Integer.parseInt((String) req.getParameter("articlesToLoad"));
        }

        //Block to decide sort order
        decideArticleOrder(req);

        String sortMethod = (String) session.getAttribute("sortMethod");
        String ascending = (String) session.getAttribute("ascending");
        //get articles from DAO with different select statements
        List<Article> articles = getArticlesFromDAO(req, noOfArticles, sortMethod, ascending);

        JSONObject obj = new JSONObject();

        for (int i = 0; i < articles.size(); i++) {
            JSONObject article = new JSONObject();
            article.put("title", articles.get(i).getTitle());
            article.put("description", articles.get(i).getDescription());
            article.put("id", articles.get(i).getId());
            article.put("userLoggedIn", session.getAttribute("username"));
            article.put("author", articles.get(i).getUserName());
            String imagePath = articles.get(i).getImages();
            if (imagePath != null) {
                article.put("image", imagePath);
            }
            obj.put(i, article);
        }
        resp.getWriter().write(obj.toJSONString());

        articleIndex += noOfArticles;
    }

    private List<Article> getArticlesFromDAO(HttpServletRequest req, int noOfArticles, String sortMethod, String ascending) throws SQLException {
        List<Article> articles = null;
        //Handle regular sorted listing
        if ((req.getParameter("searchString") == null) && (req.getParameter("userArticles").equals("false"))) {
            try (ArticleDAO articleDAO = new ArticleDAO(db)) {
                articles = articleDAO.getNextArticles(articleIndex, noOfArticles, sortMethod, ascending);
            }

            //Handle article search
        } else if (req.getParameter("searchString") != null) {
            try (ArticleDAO articleDAO = new ArticleDAO(db)) {
                articles = articleDAO.getArticlesBySearch(req.getParameter("searchString"), sortMethod, ascending);
            }
            //Handles display user's articles
        } else if ((req.getParameter("userArticles") != null) && (req.getParameter("userArticles").equals("true"))) {
            try (UserDAO userDAO = new UserDAO(db)) {
                user = userDAO.getUserByUname((String) session.getAttribute("username"));
            }
            try (ArticleDAO articleDAO = new ArticleDAO(db)) {
                articles = articleDAO.getMyArticles(user);
            }
        } else if (session.getAttribute("admin") != null) {
            try (ArticleDAO articleDAO = new ArticleDAO(db)) {
                articles = articleDAO.getAllArticles();
            }
        } else {
            try (ArticleDAO articleDAO = new ArticleDAO(db)) {
                articles = articleDAO.getNextArticles(articleIndex, noOfArticles, sortMethod, ascending);
            }
            articleIndex = 0;
        }
        return articles;
    }

    private void decideArticleOrder(HttpServletRequest req) {
        if (req.getParameter("sortMethod") == null) {
            session.setAttribute("sortMethod", "articleID");
            session.setAttribute("ascending", "DESC");
        } else if ((req.getParameter("sortMethod") != null) && (req.getParameter("sortMethod").equals("newestArticleFirst"))) {
            session.setAttribute("sortMethod", "articleID");
            session.setAttribute("ascending", "DESC");
        } else if ((req.getParameter("sortMethod") != null) && (req.getParameter("sortMethod").equals("oldestArticleFirst"))) {
            session.setAttribute("sortMethod", "articleID");
            session.setAttribute("ascending", "ASC");
        } else if ((req.getParameter("sortMethod") != null) && (req.getParameter("sortMethod").equals("titleAsc"))) {
            session.setAttribute("sortMethod", "title");
            session.setAttribute("ascending", "ASC");
        } else if ((req.getParameter("sortMethod") != null) && (req.getParameter("sortMethod").equals("titleDesc"))) {
            session.setAttribute("sortMethod", "title");
            session.setAttribute("ascending", "DESC");
        } else if ((req.getParameter("sortMethod") != null) && (req.getParameter("sortMethod").equals("unameAsc"))) {
            session.setAttribute("sortMethod", "uname");
            session.setAttribute("ascending", "ASC");
        } else if ((req.getParameter("sortMethod") != null) && (req.getParameter("sortMethod").equals("unameDesc"))) {
            session.setAttribute("sortMethod", "uname");
            session.setAttribute("ascending", "DESC");
        }
    }

    private void addNewArticle(HttpServletRequest req, HttpServletResponse resp, String username, String title, String description, String content, String imagePath,Timestamp date) throws ServletException, IOException {
        boolean editing = false;


        Article article = null;
        if (req.getParameter("articleId") == null) {
            article = new Article(username, date, title, description, content, imagePath);
        } else {
            article = new Article(Integer.parseInt(req.getParameter("articleId")), username, date, title, description, content);
            editing = true;
        }
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            articleDAO.saveArticle(article);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //if parameter is not null, article was created from profile page so we want to direct back to profile
        if (editing) {
            thumbnailToArticle(req, resp);
            return;
        } else if (req.getParameter("writingFromProfilePage") == null) {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;

        } else {
            req.getRequestDispatcher("profilePage.jsp").forward(req, resp);
            return;
        }
    }

    //saves image to folder then makes it into thumbnail size then calls generateImagePath() and saveImageToDatabase() to save image Path to db. If article info is also included, saves a new article along with media
    private void saveImagesAndArticles(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String username = (String) session.getAttribute("username"); //get author
        String title = "";
        String description = "";
        String content = "";
        int articleid = 0;
        Timestamp date = new Timestamp(System.currentTimeMillis());

        ServletContext servletContext = getServletContext();
        String filePath = servletContext.getRealPath("/Uploaded-Photos");
        File file;
        String imagePaths = "";
        String articleidString = "";

        int maxFileSize = 500 * 1024 * 1024;
        int maxMemSize = 5000 * 1024 * 1024;

        File uploadedFolder = new File(filePath);
        if (!uploadedFolder.exists()) {
            uploadedFolder.mkdirs();
        }

        // Check that we have a file upload request
        //Boolean isMultipart = ServletFileUpload.isMultipartContent(req);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);

        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("c:\\temp"));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        upload.setSizeMax(maxFileSize);

        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(req);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();
            String inputName = "";
            String fileName = "";


            while (i.hasNext()) {

                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    //getting the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    fileName = fi.getName();
                    //writing file into out folder
                    file = writeFile(filePath, fileName);
                    fi.write(file);

                    //generating final paths and adding to ArrayList
                    String userImagePath = generateImagePath(fileName, filePath);
                    if (imagePaths.equals("")) {
                        imagePaths += userImagePath;
                    } else {
                        imagePaths += "," + userImagePath;
                    }

                } else {
                    inputName = fi.getFieldName();
                    if (inputName.equalsIgnoreCase("articleid")) {
                        articleidString = fi.getString();
                        articleid = Integer.parseInt(articleidString);
                    }
                    if (inputName.equalsIgnoreCase("title")) {
                        title = fi.getString();
                    }
                    if (inputName.equalsIgnoreCase("recipedescrip")) {
                        description = fi.getString();
                    }
                    if (inputName.equalsIgnoreCase("articlecontent")) {
                        content = fi.getString();
                    }
                    if (inputName.equalsIgnoreCase("dateandtime")){
                        String datetime = fi.getString();
                        if(!datetime.equals("")) {
                            date = Timestamp.valueOf(datetime);
                        } else date = new Timestamp(System.currentTimeMillis());
                    }

                }

            }
        } catch (Exception ex) {
            out.print("<p>Sorry, your file is too big</p>");
            return;
        }

        //save the path to the image in the database
        if (articleid != 0) {
            saveImagePath(req, resp, username, articleid, servletContext, imagePaths);
            return;
        }
        //create new article with media
        else {
            addNewArticle(req, resp, username, title, description, content, imagePaths, date);
        }


    }

    private void saveImagePath(HttpServletRequest req, HttpServletResponse resp, String username, int articleid, ServletContext servletContext, String imagePaths) throws SQLException, IOException {
        Article article;

        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            article = articleDAO.getArticleById(""+ articleid);
        }
        //get existing image paths from database, if any
        String previousImagePaths = null;
        if (article != null) {
            previousImagePaths = article.getImages();
        }

        if (previousImagePaths != null) {
            imagePaths = imagePaths + "," + previousImagePaths;
        }
        String finalImagePath = saveImagePathToDatabase(imagePaths, articleid);

        //send req object to jsp to display the images
        req.setAttribute("images", finalImagePath);

        String author = article.getUserName();
        resp.sendRedirect("ArticleServlet?articleid=" + articleid + "&userLoggedIn=" + username + "&author=" + author);
    }

    private File writeFile(String filePath, String fileName) {
        File file;// Write the file
        if (fileName.lastIndexOf("\\") >= 0) {

            if (filePath.charAt(0) == '/') {
                //mac pathing
                file = new File(filePath + "/" + fileName.substring(fileName.lastIndexOf("/") + 1));
            } else {
                //windows pathing
                file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
            }
        } else {

            if (filePath.charAt(0) == '/') {
                //mac pathing
                file = new File(filePath + "/" + fileName.substring(fileName.lastIndexOf("/") + 1));
            } else {
                //windows pathing
                file = new File(filePath + "\\" + fileName.substring(fileName.lastIndexOf("\\") + 1));
            }
        }
        return file;
    }

    private void thumbnailToArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String articleId = (String) session.getAttribute("articleid");
        Article article = null;
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            article = articleDAO.getArticleById(articleId);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        req.setAttribute("title", article.getTitle());
        req.setAttribute("description", article.getDescription());
        req.setAttribute("content", article.getContent());
        req.setAttribute("date", article.getDate());
        req.setAttribute("user", article.getUserName());
        req.setAttribute("id", article.getId());
        session.setAttribute("Author", article.getUserName());

        //processing images to load with article
        String allImagePaths = article.getImages();
        if (allImagePaths != null) {
            String[] imagesArray = allImagePaths.split(",");
            int numOfImages = imagesArray.length - 1;
            for (int i = 0; i < imagesArray.length; i++) {
                req.setAttribute("image" + i, imagesArray[i]);
            }
            req.setAttribute("numOfImages", numOfImages);
        }

        //getting avatar of author to load with article
        try (UserDAO userDAO = new UserDAO(db)) {
            user = userDAO.getUserByUname(article.getUserName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String avatar = user.getAvatar();
        if (avatar != null) {
            req.setAttribute("avatar", avatar);
        }

        req.getRequestDispatcher("article.jsp").forward(req, resp);
    }

    private void imagesAjaxResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Article article = null;
        //get article from database using article id
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            String articleidString = req.getParameter("articleid");
            article = articleDAO.getArticleById(articleidString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String userName = article.getUserName();
        try (UserDAO userDAO = new UserDAO(db)) {
            user = userDAO.getUserByUname(userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        obj.put("Avatar", user.getAvatar());
        obj.put("Images", article.getImages());
        obj.put("Author", article.getUserName());

        if (!obj.isEmpty()) {
            String object = obj.toJSONString();
            resp.getWriter().write(object);
        }

    }


    /**
     * generate path to the image picked by the user
     */
    private String generateImagePath(String fileName, String filePath) throws IOException, SQLException {

        String userAvatarPath;
        if (filePath.charAt(0) == '/') {
            //mac pathing
            userAvatarPath = "Uploaded-Photos/" + fileName;
        } else {
            //windows pathing
            userAvatarPath = "Uploaded-Photos\\" + fileName;
        }
        return userAvatarPath;
    }


    /**
     * save the path to the avatar associated with the user in the database
     */

    private String saveImagePathToDatabase(String imagePath, int articleID) throws IOException, SQLException {
        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            articleDAO.addImages(articleID, imagePath);
        }
        return imagePath;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
