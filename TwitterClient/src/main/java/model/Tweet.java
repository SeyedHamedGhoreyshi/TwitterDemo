package model;

import java.time.LocalDateTime;

public class Tweet {

    private String tweetText ;
    private int numLikes ;
    private int numRetweets ;
    private int numComments ;
    private String userId ;
    private String tweetId ;
    private String date ;
    private String userName ;
    private String token ;
    private String avatar ;

    public Tweet(String tweetText, String userName, String date, String token  , String profile) {
        this.tweetText = tweetText;
        this.numLikes = 15 ;
        this.numRetweets = 0;
        this.numComments = 0;
        this.userName = userName ;
        this.date = date;
        this.token = token;
        this.avatar = profile ;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public int getNumRetweets() {
        return numRetweets;
    }

    public void setNumRetweets(int numRetweets) {
        this.numRetweets = numRetweets;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
