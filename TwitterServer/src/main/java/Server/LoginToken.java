package Server;

public class LoginToken {
    private String respone ;
    private String token ;

    public LoginToken(String respone, String token) {
        this.respone = respone;
        this.token = token;
    }

    public String getRespone() {
        return respone;
    }

    public void setRespone(String respone) {
        this.respone = respone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
