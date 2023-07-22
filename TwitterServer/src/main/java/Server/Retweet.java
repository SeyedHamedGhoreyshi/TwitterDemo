package Server;

import java.time.LocalDateTime;

public class Retweet extends Tweet{
    private String reTweetId ;


    public Retweet(String tweetText, String userId, String date, String token , String avatar) {
        super(tweetText, userId, date, token , avatar);
    }

    public String getReTweetId() {
        return reTweetId;
    }



}
