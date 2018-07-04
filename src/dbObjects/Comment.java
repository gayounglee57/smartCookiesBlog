package dbObjects;
import java.sql.Timestamp;


/**
 * Represents an article.
 */
public class Comment {

    private int id = 0;
    private int articleId;
    private String userName;
    private String comment;
    private Timestamp dateTime;
    private int parentId;
    private int childId;
    private boolean hidden;


    public Comment(int id, int articleId, String userName, String comment, Timestamp dateTime,boolean hidden,int parentId,int childId) {
        this.id = id;
        this.articleId = articleId;
        this.userName = userName;
        this.comment = comment;
        this.dateTime = dateTime;
        this.parentId = parentId;
        this.childId = childId;

    }

    public Comment(int articleId, String userName, String comment, Timestamp dateTime,int parentId,int childId) {
        this.articleId = articleId;
        this.userName = userName;
        this.comment = comment;
        this.dateTime = dateTime;
        this.parentId = parentId;
        this.childId = childId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String commenSt) {
        this.comment = comment;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }
}
