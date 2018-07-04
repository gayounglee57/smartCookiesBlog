package dbObjects;

import java.sql.Timestamp;

/**
 * Represents an article.
 */
public class User {

    private String uname;
    private String fname;
    private String lname;
    private String email;
    private String dob;
    private String country;
    private String description;
    private String avatar;
    private String password;
    private Boolean admin;
    private Timestamp expiration;
    
    
    public User(String uname, String fname, String lname,String email,String dob,String country, String description,String avatar,String password) {
        this.uname = uname;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.dob = dob;
        this.country = country;
        this.description = description;
        this.avatar = avatar;
        this.password = password;
    }
    public User(String uname, String fname, String lname,String email,String dob,String country, String description,String avatar,String password, Boolean admin) {
        this.uname = uname;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.dob = dob;
        this.country = country;
        this.description = description;
        this.avatar = avatar;
        this.password = password;
        this.admin = admin;
    }
    public Timestamp getExpiration() { return expiration; }

    public void setExpiration(Timestamp expiration) { this.expiration = expiration; }

    public Boolean getAdmin() { return admin; }

    public void setAdmin(boolean admin) { this.admin = admin; }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public Integer getArticles() {
//        return articles;
//    }
//
//    public void setArticles(Integer articles) {
//        this.articles = articles;
//    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
