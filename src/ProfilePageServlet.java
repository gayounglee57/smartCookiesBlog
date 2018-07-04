
import dao.UserDAO;
import db.MySQLDatabase;
import dbObjects.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Created by glee156 on 23/01/2018.
 */
public class ProfilePageServlet extends HttpServlet {

    private String filePath;
    private int maxFileSize = 50 * 1024 * 1024;
    private int maxMemSize = 4 * 1024 * 1024;
    private File file;
    private ServletContext servletContext;
    private MySQLDatabase db;
    private User user;


    /**
     * If user chooses an avatar file from the pre-defined set, saves the path to database. Or if user clicks avatar to their profile,
     * does a get request to this Servlet to show saved info.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        this.servletContext = getServletContext();
        this.db = new MySQLDatabase(servletContext);

        HttpSession session = req.getSession();
        session.setAttribute("source", "profile");
        //if not logged in, go to homepage
        if (session.getAttribute("username") == null){
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }
        //This block handles displaying user info
        if (req.getParameter("image") == null) {
            //get username of user logged in and get User object associated with the username
            getUserDetails(req, resp);
        //This block handles saving avatar path to database when user chooses an avatar from pre-defined set
        } else if (req.getParameter("image") != null) {
            setAvatar(req, resp);
        }

    }

    //saving avatar path to database when user chooses an avatar from pre-defined set
    private void setAvatar(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Map<String, String[]> param = req.getParameterMap();
        String path = param.get("image")[0];

        //create path to image the user selected
        String imagePath = "images\\" + path;

        //get username
        String uname = (String) req.getSession().getAttribute("username");

        //send imagePath to database for user who selected the image
        try {
            saveImagePathToDatabase(servletContext, imagePath, uname);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.setAttribute("avatar", imagePath);
        req.getRequestDispatcher("profilePage.jsp").forward(req, resp);
    }

    //responding to ajax call to display user info
    private void getUserDetails(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String usernameAttribute = (String) req.getSession().getAttribute("username");

        try (UserDAO userDAO = new UserDAO(db)) {
            user = userDAO.getUserByUname(usernameAttribute);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();
        //get all saved info on the user
        String username = user.getUname();
        obj.put("Username", username);
        String firstname = user.getFname();
        obj.put("First Name", firstname);
        String lastname = user.getLname();
        obj.put("Last Name", lastname);
        String email = user.getEmail();
        obj.put("Email", email);
        String dob = user.getDob();
        obj.put("D.O.B", dob);
        String country = user.getCountry();
        obj.put("Country", country);
        String description = user.getDescription();
        obj.put("Description", description);
        String password = user.getPassword();
        obj.put("Password", password);
        String avatar = user.getAvatar();
        //gives cookies_outline as avatar if user has not chosen an avatar
        if (avatar == null) {
            avatar = "cookies_outline.png";
        }
        obj.put("Avatar", avatar);
        String object = obj.toJSONString();
        resp.getWriter().write(object);
    }

    /**
     * If user chooses an avatar file from their own computer, saves the avatar image in a folder called "Uploaded-Photos" and saves the path to database.
     *
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        filePath = servletContext.getRealPath("/Uploaded-Photos");

        File uploadedFolder = new File(filePath);
        if(!uploadedFolder.exists()){
            uploadedFolder.mkdirs();
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);

        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("c:\\temp"));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );

        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(req);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            while ( i.hasNext () ) {
                FileItem fi = (FileItem)i.next();

                if ( !fi.isFormField () ) {
                    // Get the uploaded file parameters
                    String fileName = writeFile(fi);

                    //create thumbnail version of avatar
                    convertToThumbnail();

                    //save the path to the avatar associated with the user in the database
                    String userAvatarPath = generateAvatarPath(req, servletContext, fileName);

                    //send req object to jsp to display the avatar
                    req.setAttribute("avatar", userAvatarPath);
                    req.getRequestDispatcher("profilePage.jsp").forward(req, resp);
                }

            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private String writeFile(FileItem fi) throws Exception {
        String fileName = fi.getName();

        if( fileName.lastIndexOf("\\") >= 0 ) {
            file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
        } else {
            file = new File( filePath + "\\" + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
        }
        fi.write( file ) ;
        return fileName;
    }


    /**
     * converts the image user has chosen to a thumbnail
     */
    private void convertToThumbnail() throws IOException {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        img.createGraphics().drawImage(ImageIO.read(file).getScaledInstance(100, 100, Image.SCALE_SMOOTH),0,0,null);
        ImageIO.write(img, "jpg", file);
    }


    /**
     * generate path to the avatar picked by the user
     */
    private String generateAvatarPath(HttpServletRequest req, ServletContext servletContext, String fileName) throws IOException, SQLException {

        String userAvatarPath = "Uploaded-Photos\\" + fileName;
        String userName = (String)req.getSession().getAttribute("username");
        return saveImagePathToDatabase(servletContext, userAvatarPath, userName);
    }


    /**
     * save the path to the avatar associated with the user in the database
     */

    private String saveImagePathToDatabase(ServletContext servletContext, String userAvatarPath, String userName) throws IOException, SQLException {
        MySQLDatabase db = new MySQLDatabase(servletContext);
        try(UserDAO userDAO = new UserDAO(db)) {
            userDAO.setAvatarPath(userName, userAvatarPath);
        }
        return userAvatarPath;
    }

}
