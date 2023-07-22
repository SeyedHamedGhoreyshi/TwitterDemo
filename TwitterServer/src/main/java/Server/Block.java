package Server;

public class Block {
    private String blocker ;
    private String blocked ;
    private String token ;

    public Block( String blocker, String blocked , String token) {

        this.blocker = blocker;
        this.blocked = blocked;
        this.token = token ;
    }



    public String getBlocker() {
        return blocker;
    }

    public void setBlocker(String blocker) {
        this.blocker = blocker;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
