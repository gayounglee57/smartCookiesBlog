


/**
 * Created by tpre939 on 22/01/2018.
 */

import dao.ArticleDAO;
import dao.UserDAO;
import db.MySQLDatabase;
import dbObjects.Article;
import dbObjects.User;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MultimediaServlet extends HttpServlet {
    private HttpSession session;
    private MySQLDatabase db;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.db = new MySQLDatabase(getServletContext());
        String paths = "";
        session = req.getSession();

            //This block handles deleting images, videos and audios
            if (req.getParameter("deletePhotoButton") != null) {
                deleteMultimedia(req, resp);
                return;
            }
            //This block handles adding a youtube link to the database
            else if (req.getParameter("youtubeLink") != null) {
                addYoutubeLink(req, resp);
                return;
            }
            //This block handles sending a response back to the ajax call for youtube links
            else if (req.getParameter("youtubeAjax") != null) {
                sendYoutubeAjax(req, resp);
            }
            //This block handles displaying (setting attributes for) gallery.jsp
            else if(req.getParameter("allMedia") != null){
                String allMedia = req.getParameter("allMedia");

                //shows all media
                if(allMedia.equals("true")){
                        paths = getAllMedia();
                        sortAndSetAttribute(req, paths);

                }
                //shows only the logged-in user's media
                else{
                    //dealing with images
                    paths = getUserMedia(req);
                    sortAndSetAttribute(req, paths);
                }

                req.getRequestDispatcher("gallery.jsp").forward(req, resp);
            }

    }

    private void sendYoutubeAjax(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String articleid = req.getParameter("articleid");
        Article article = null;

        try(ArticleDAO articleDAO = new ArticleDAO(db)){
            article = articleDAO.getArticleById(articleid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        obj.put("Youtube", article.getYoutubeLink());
        obj.put("Author", article.getUserName());
        obj.put("ArticleId", articleid);

        if (obj.size() != 0) {
            String object = obj.toJSONString();
            resp.getWriter().write(object);
        }
    }

    private void addYoutubeLink(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String rawLink = req.getParameter("youtube-url");
            String embedLink = rawLink.replace("watch?v=", "embed/");
            if(embedLink.contains("<iframe>")){
                embedLink = "";
            }

            String articleidString = req.getParameter("articleid");
            int articleid = Integer.parseInt(articleidString);

        try(ArticleDAO articleDAO = new ArticleDAO(db)){
            articleDAO.addYoutubeLink(embedLink, articleid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //redirect user back to same article page after deleting their comment
        String username = req.getParameter("userLoggedIn");
        String author = req.getParameter("author");
        req.getRequestDispatcher("ArticleServlet?articleid=" + articleid + "&userLoggedIn=" + username + "&author=" + author).forward(req, resp);

    }

    private void deleteMultimedia(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Article article = null;
        String articleId = req.getParameter("articleid");
        Map<String, String[]> map = req.getParameterMap();

        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            article = articleDAO.getArticleById(articleId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String currentImagePaths = article.getImages();
        String[] currentImagePathsArray = currentImagePaths.split(",");
        String deleteImagePaths = "";

        //finding the paths of images selected to be deleted
        for (String s : map.keySet()) {
            if (!s.equals("deletePhotoButton") && !s.equals("articleid")) {
                if (deleteImagePaths.equals("")) {
                    deleteImagePaths += map.get(s)[0];
                } else {
                    deleteImagePaths = deleteImagePaths + "," + map.get(s)[0];
                }
            }
        }
        String[] deleteImagePathsArray = deleteImagePaths.split(",");
        String finalImagePaths = getNewPathString(currentImagePathsArray, deleteImagePathsArray);

        try (ArticleDAO articleDAO = new ArticleDAO(db)) {
            articleDAO.addImages(Integer.parseInt(articleId), finalImagePaths);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //redirect user back to same article page after deleting their comment
        String username = req.getParameter("userLoggedIn");
        String author = req.getParameter("author");
        req.getRequestDispatcher("ArticleServlet?articleid=" + articleId + "&userLoggedIn=" + username + "&author=" + author).forward(req, resp);
    }

    private String getNewPathString(String[] currentImagePathsArray, String[] deleteImagePathsArray) {
        String finalImagePaths = "";

        //creating new string of image paths for database which excludes those selected to be deleted
        outer:
        for (int i = 0; i < currentImagePathsArray.length; i++) {
            for (int j = 0; j < deleteImagePathsArray.length; j++) {
                if (currentImagePathsArray[i].equals(deleteImagePathsArray[j])) {
                    continue outer;
                }
            }
            if (finalImagePaths.equals("")) {
                finalImagePaths = currentImagePathsArray[i];
            } else {
                finalImagePaths = finalImagePaths + "," + currentImagePathsArray[i];
            }
        }
        return finalImagePaths;
    }

    private String getAllMedia(){
        List<Article> articleList = null;
        try(ArticleDAO articleDAO = new ArticleDAO(db)){
            articleList = articleDAO.getAllArticles();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String paths = "";
        for(Article a: articleList){
            paths += (a.getImages() + ",");
        }

        return paths.substring(0, paths.length() -1);
    }

    private String getUserMedia(HttpServletRequest req){
        List<Article> articleList = null;
        String username = (String)req.getSession().getAttribute("username");
        User user = null;
        String finalPath = "";

        try(UserDAO userDAO = new UserDAO(db)){
            user = userDAO.getUserByUname(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(user != null) {
            try (ArticleDAO articleDAO = new ArticleDAO(db)) {
                articleList = articleDAO.getMyArticles(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //go through each Article object in list to get images of each article
            String paths = "";
            for (Article a : articleList) {
                paths += (a.getImages() + ",");
            }
            //if there are images in any article in list, get rid of comma at end of string
            if(paths.length() > 0) {
                finalPath = paths.substring(0, (paths.length() - 1));
            }
        }
        return finalPath;
    }

    //attributes for gallery.jsp
    private void setGalleryAttributes(String paths, String id, HttpServletRequest req) throws ServletException, IOException {
        String[] imagesArray = paths.split(",");
        int numMedia = -1;

        for (int i = 0; i < imagesArray.length; i++) {
            String currentMedia = imagesArray[i];
            if(!currentMedia.equals("")) {
                String currentPath = imagesArray[i];
                req.setAttribute(id + i, currentPath);
                numMedia++;
            }
        }

        req.setAttribute("num" + id, numMedia);

    }

    //sorts list depending on file extensions then calls method to set attributes for gallery.jsp
    private String sortList(String paths, String ext, HttpServletRequest req) throws ServletException, IOException {
        String sortedString = "";

        String[] imagesArray = paths.split(",");

        for (int i = 0; i < imagesArray.length; i++) {
            String currentPath = imagesArray[i];
            if(currentPath.contains("null")){
                continue;
            }
            else if(currentPath.contains(ext)){
                sortedString += (currentPath + ",");
            }
        }

        if(sortedString.length() > 0) {
            sortedString = sortedString.substring(0, sortedString.length() -1);
        }

        setGalleryAttributes(sortedString, ext, req);

        return sortedString;
    }

    //calls method to sort list depending on file extensions (which then calls method to set attributes)
    private void sortAndSetAttribute(HttpServletRequest req, String paths) throws ServletException, IOException {
        String[] extensionArray = {"jpg", "jpeg", "png", "mp3", "wav", "ogg", "mp4", "webm"};

        for (int i = 0; i < extensionArray.length; i++) {
            sortList(paths, extensionArray[i], req);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}