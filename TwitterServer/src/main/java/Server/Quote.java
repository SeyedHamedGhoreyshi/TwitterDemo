package Server;

public class Quote extends Tweet {
    private String quotedId ;

    public Quote(String tweetText, String userId, String date, String token ,   String avatar) {
        super(tweetText, userId, date, token , avatar);
    }

    public String getQuotedId() {
        return quotedId;
    }
}
