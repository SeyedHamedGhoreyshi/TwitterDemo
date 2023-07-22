package Server;

public class Follow {
    private String userName ;
    private String userFollowedName ;
    private String token ;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFollowedName() {
        return userFollowedName;
    }

    public void setUserFollowedName(String userFollowedName) {
        this.userFollowedName = userFollowedName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
