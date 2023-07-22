package model;


import java.time.LocalDateTime;

public class Quote extends Tweet{
    private String quotedId ;


    public Quote(String tweetText, String userId, String date, String token , String avatar , String quotedId) {
        super(tweetText, userId, date, token , avatar);
        this.quotedId = quotedId ;
    }

    public String getQuotedId() {
        return quotedId;
    }




    public void setQuotedId(String quotedId) {
        this.quotedId = quotedId;
    }
}
