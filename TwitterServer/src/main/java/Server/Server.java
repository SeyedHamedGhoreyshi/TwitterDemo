package Server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.*;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;


public class Server implements Runnable {


    private ArrayList<ConnectionHandler> connectionHandlers;
    private ServerSocket serverSocket;
    private boolean done;
    private ExecutorService pool;


    public Server() {
        connectionHandlers = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = serverSocket.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(client);
                pool.execute(connectionHandler);
            }
        } catch (IOException e) {
            shutDown();
        }

    }

    public void shutDown() {
        try {
            done = true;
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {

        }


    }


    class ConnectionHandler implements Runnable {
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        String secretKey = "mySecretKey";


        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        private String generateToken(String userName) {

            Date expirationDate = new Date(System.currentTimeMillis() + 3600000); // 1 hour from now

            String token = Jwts.builder()
                    .setSubject(userName)
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();
            return token;
        }

        private boolean verifyToken(String token) {
            try {
                Jws<Claims> claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token);

                // Check if token is expired
                Date expirationDate = claims.getBody().getExpiration();
                if (expirationDate.before(new Date())) {
                    return false;
                }

                return true;
            } catch (Exception e) {
                // Invalid token
                return false;
            }
        }


        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                Gson gson = new Gson();
                String type = in.readLine();


                if (type.equals("signup")) {
                    Signup signup = gson.fromJson(in.readLine(), Signup.class);
                    String response = UserManagement.signUp(signup.getFirstName(), signup.getLastName(), signup.getUserId(), signup.getPassword(), signup.getRePassword(), signup.getPhoneNumber(), signup.getCountry(), signup.getBirthDate(), signup.getEmail());
                    out.println(response);

                } else if (type.equals("login")) {
                    Login login = gson.fromJson(in.readLine(), Login.class);
                    String respone = UserManagement.logIn(login.getUserId(), login.getPassword());
                    if (respone.equals("success")) {
                        String token = generateToken(login.getUserId());
                        LoginToken loginToken = new LoginToken(respone, token);
                        String json = gson.toJson(loginToken);
                        out.println(json);


                    } else if (respone.equals("fail")) {
                        LoginToken loginToken = new LoginToken(respone, "fail");
                        String json = gson.toJson(loginToken);
                        out.println(json);
                    }


                } else if (type.equals("setAvatar")) {
                    User user = gson.fromJson(in.readLine(), User.class);
                    String token = user.getToken();
                    if (verifyToken(token)) {
                        String receivedImageAsString = user.getAvatar();
                        String response = UserManagement.setAvatar(receivedImageAsString, user.getUserName());
                        String response2 = UserManagement.changeTweetAvatar(receivedImageAsString, user.getUserName());
                        String response3 = UserManagement.changeCommentAvatar(receivedImageAsString , user.getUserName()) ;
                        if (response.equals("success") && response2.equals("success") && response3.equals("success"))
                            out.println(response);
                        else out.println("fail");
                    } else {
                        out.println("invalid token");
                    }

                } else if (type.equals("search")) {
                    String key = in.readLine() ;
                    if (key.charAt(0) == '#'){
                        ArrayList<Tweet> tweets = UserManagement.searchHashtag(key);
                        String json = gson.toJson(tweets);
                        out.println(json);
                    }
                    else {
                        ArrayList<User> users = UserManagement.searchUser(key);
                        String json = gson.toJson(users);
                        out.println(json);
                    }

                } else if (type.equals("setHeader")) {
                    User user = gson.fromJson(in.readLine(), User.class);
                    String token = user.getToken();
                    if (verifyToken(token)) {
                        String receivedImageAsString = user.getHeader();
                        String response = UserManagement.setHeader(receivedImageAsString, user.getUserName());
                        out.println(response);
                    } else {
                        out.println("invalid token");
                    }

                } else if (type.equals("setBio")) {
                    User user = gson.fromJson(in.readLine(), User.class);
                    String token = user.getToken();

                    if (verifyToken(token)) {
                        String response = UserManagement.setBio(user.getBio(), user.getUserName());
                        out.println(response);

                    } else out.println("invalid token");


                } else if (type.equals("getUser")) {
                    String userName = in.readLine();
                    User user = UserManagement.getUser(userName);
                    String json = gson.toJson(user);
                    out.println(json);
                } else if (type.equals("follow")) {
                    Follow follow = gson.fromJson(in.readLine() ,Follow.class) ;
                    String token = follow.getToken() ;
                    if (verifyToken(token)){
                        String response = UserManagement.follow(follow.getUserName() , follow.getUserFollowedName()) ;
                        out.println(response);

                    }
                    else out.println("token is invalid");


                } else if (type.equals("unFollow")) {
                    Follow follow = gson.fromJson(in.readLine() ,Follow.class) ;
                    String token = follow.getToken() ;
                    if (verifyToken(token)){
                        String response = UserManagement.unFollow(follow.getUserName() , follow.getUserFollowedName()) ;
                        out.println(response);

                    }
                    else out.println("token is invalid");

                } else if (type.equals("tweet")) {
                    Tweet tweet = gson.fromJson(in.readLine(), Tweet.class);
                    String token = tweet.getToken();

                    if (verifyToken(token)) {
                        String respone = UserManagement.tweet(tweet.getUserName(), tweet.getTweetText(), tweet.getAvatar());
                        out.println(respone);
                    } else {
                        out.println("token is invalid");
                    }

                } else if (type.equals("getTimeLine")) {
                    String userName = in.readLine();
                    ArrayList<Tweet> tweets = UserManagement.TimeLine(userName);
                    String json = gson.toJson(tweets);
                    out.println(json);

                } else if (type.equals("getComments")) {
                    String tweetId = in.readLine() ;
                    ArrayList<Reply> replies = UserManagement.commentView(Integer.parseInt(tweetId));
                    String json = gson.toJson(replies);
                    out.println(json);

                } else if (type.equals("reTweet")) {
                    Retweet retweet = gson.fromJson(in.readLine() , Retweet.class) ;
                    String token = retweet.getToken() ;
                    if (verifyToken(token)) {
                        String respone = UserManagement.reTweet(retweet.getUserName(),Integer.parseInt(retweet.getReTweetId()) ,retweet.getAvatar());
                        out.println(respone);
                    } else {
                        out.println("token is invalid");
                    }




                } else if (type.equals("getTweet")) {
                    String tweetId = in.readLine();
                    Tweet tweet = UserManagement.getTweet(Integer.parseInt(tweetId)) ;
                    String json = gson.toJson(tweet) ;
                    out.println(json);
                } else if (type.equals("quote")) {
                    Quote quote =gson.fromJson(in.readLine() , Quote.class) ;
                    String token = quote.getToken() ;
                    if (verifyToken(token)){
                        String response = UserManagement.Quote(quote.getUserName() ,Integer.parseInt(quote.getQuotedId()) ,quote.getTweetText() ,quote.getAvatar());
                        out.println(response);

                    }
                    else out.println("invalid token");


                } else if (type.equals("reply")) {
                    Reply reply = gson.fromJson(in.readLine(), Reply.class);
                    String token = reply.getToken();
                    if (verifyToken(token)) {
                        String response = UserManagement.Replies(reply.getUserName(), reply.getReplyText(), reply.getProfile(), Integer.parseInt(reply.getReplyId()));
                        out.println(response);
                    } else out.println("token is invalid");

                } else if (type.equals("followCheck")) {
                    Follow follow = gson.fromJson(in.readLine() , Follow.class) ;
                    String token = follow.getToken() ;
                    if(verifyToken(token)){
                        if (UserManagement.followCheck(follow.getUserName() , follow.getUserFollowedName())) out.println("yes");
                        else out.println("no");

                    }
                    else {
                        out.println("token is invalid");
                    }

                } else if (type.equals("getFollowings")) {
                    String userName = in.readLine() ;
                    List<User> users = UserManagement.followingView(userName) ;
                    String json = gson.toJson(users);
                    out.println(json);


                } else if (type.equals("getFollowers")) {
                    String userName = in.readLine() ;
                    List<User> users = UserManagement.followerView(userName) ;
                    String json = gson.toJson(users);
                    out.println(json);



                } else if (type.equals("block")) {
                    Block block = gson.fromJson(in.readLine() ,Block.class) ;
                    String token = block.getToken() ;
                    if (verifyToken(token)){
                        String response = UserManagement.blocking(block.getBlocker() , block.getBlocked()) ;
                        out.println(response);
                    }
                    else {
                        out.println("token is invalid");

                    }


                } else if (type.equals("unBlock")) {
                    Block block = gson.fromJson(in.readLine() ,Block.class) ;
                    String token = block.getToken() ;
                    if (verifyToken(token)){
                        String response = UserManagement.unBlocking(block.getBlocker() , block.getBlocked()) ;
                        out.println(response);
                    }
                    else {
                        out.println("token is invalid");

                    }

                } else if (type.equals("checkBlock")) {
                    Block block = gson.fromJson(in.readLine() ,Block.class) ;
                    String token = block.getToken() ;
                    if (verifyToken(token)){
                        if (UserManagement.checkBlock(block.getBlocker() , block.getBlocked())) {
                            out.println("yes");
                        }
                        else out.println("no");

                    }else out.println("token is invalid");

                }


            } catch (IOException e) {
                shutDown();
            } catch (SerialException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


        public void shutDown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }

            } catch (IOException e) {

            }

        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}


