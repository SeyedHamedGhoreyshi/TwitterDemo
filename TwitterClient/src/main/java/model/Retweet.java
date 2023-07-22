package model;

import java.time.LocalDateTime;

public class Retweet extends Tweet {

    private String reTweetId;


    public Retweet(String tweetText, String userId, String date, String token, String avatar ,String reTweetId) {
        super(tweetText, userId, date, token, avatar);
        this.reTweetId = reTweetId ;
    }

    public String getReTweetId() {
        return reTweetId;
    }

    public void setReTweetId(String reTweetId) {
        this.reTweetId = reTweetId;
    }
}



