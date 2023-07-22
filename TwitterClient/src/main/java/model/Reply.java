package model;

import java.time.LocalDateTime;

public class Reply {

    private String replyId ;
    private String replyText ;
    private String userName ;
    private String date ;
    private String token ;
    private String profile ;

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Reply(String replyText, String userName, String date, String token, String profile, String replyId) {
        this.replyId = replyId;
        this.replyText = replyText;
        this.userName = userName;
        this.date = date;
        this.token = token;
        this.profile = profile;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getReplyId() {
        return replyId;
    }


}
