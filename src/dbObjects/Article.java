package dbObjects;
import java.sql.Timestamp;

/**
 * Represents an article.
 */
public class Article {

    private Integer id;
    private String userName;
    private Timestamp date;
    private String content;
    private String title;
    private String description;
    private String images;
    private Integer hide;
    private String youtubeLink;

    public Article(String userName, Timestamp date, String title, String description, String content, String images) {
        System.out.println("creating new article");
        this.userName = userName;
        this.date = date;
        this.content = content;
        this.title = title;
        this.description = description;
        this.images = "";
        this.hide = 0;
        this.images = images;
    }

    public Article(Integer id, String userName, Timestamp date, String title, String description, String content) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.content = content;
        this.title = title;
        this.description = description;
        this.images = "";
        this.hide = 0;
    }

    public Article(Integer id, String userName, Timestamp date, String title, String description, String content, String images, Integer hide, String youtubeLink) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.content = content;
        this.title = title;
        this.description = description;
        this.images = images;
        this.hide = 0;
        this.youtubeLink = youtubeLink;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getHide() {
        return hide;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }
}
