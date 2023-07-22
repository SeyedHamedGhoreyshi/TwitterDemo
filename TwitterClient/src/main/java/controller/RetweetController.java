package controller;

import com.google.gson.Gson;
import com.twitter.twitterclient.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.MyClient;
import model.Retweet;
import model.Tweet;
import model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RetweetController implements Initializable {
    @FXML
    private Label QuoteIcon;
    @FXML
    private ToolBar quoteBTN;
    @FXML
    private ToolBar retweetBTN;
    @FXML
    private Label errorLabel;

    private Tweet getTweet() throws IOException {
        StringBuilder tweetId = new StringBuilder();
        File tweetIdFile = new File("tweetId.txt");
        FileReader tifr = new FileReader(tweetIdFile.getAbsoluteFile());
        BufferedReader tibr = new BufferedReader(tifr);
        String tiline;
        while ((tiline = tibr.readLine()) != null) {
            tweetId.append(tiline);
        }
        tibr.close();
        MyClient myClient = new MyClient();
        String response = myClient.sendRequest(tweetId.toString(), "getTweet");
        Gson gson = new Gson();
        Tweet tweet = gson.fromJson(response, Tweet.class);
        return tweet;

    }
//
//    private String getAvatar() throws IOException {
//        StringBuilder userName = new StringBuilder();
//        File userNamefile = new File("userName.txt");
//        FileReader fru = new FileReader(userNamefile.getAbsoluteFile());
//        BufferedReader bru = new BufferedReader(fru);
//        String userLine;
//        while ((userLine = bru.readLine()) != null) {
//            userName.append(userLine);
//        }
//        bru.close();
//        MyClient myClient = new MyClient();
//        String response = myClient.sendRequest(userName.toString(), "getUser");
//        Gson gson = new Gson();
//        User user = gson.fromJson(response, User.class);
//        String receivedAvatarAsString = user.getAvatar();
//        return receivedAvatarAsString;
//    }


    public void initialize(URL url, ResourceBundle resourceBundle) {

        quoteBTN.setOnMouseClicked(mouseEvent -> {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("quotePage.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();

            stage.setScene(scene);
            Stage currentStage = (Stage) QuoteIcon.getScene().getWindow();
            Stage previousStage = (Stage) currentStage.getOwner();
            previousStage.hide();
            currentStage.hide();
            stage.show();

        });
        retweetBTN.setOnMouseClicked(mouseEvent -> {
            try {
                StringBuilder token = new StringBuilder();
                File tokenFile = new File("token.txt");
                FileReader tfr = new FileReader(tokenFile.getAbsoluteFile());
                BufferedReader tbr = new BufferedReader(tfr);
                String tline;
                while ((tline = tbr.readLine()) != null) {
                    token.append(tline);
                }
                tbr.close();
                MyClient myClient = new MyClient();
                Tweet tweet = getTweet();
                Retweet retweet = new Retweet(tweet.getTweetText(), tweet.getUserName(), tweet.getDate(), token.toString(), tweet.getAvatar() , tweet.getTweetId());
                Gson gson = new Gson();
                String json = gson.toJson(retweet);
                String response = myClient.sendRequest(json, "reTweet");
                if (response.equals("token is invalid")) {
                    errorLabel.setText("token is invalid");
                    errorLabel.setTextFill(Color.RED);
                } else if (response.equals("success")) {
                    errorLabel.setText("success");
                    errorLabel.setTextFill(Color.GREEN);
                } else if (response.equals("fail")) {
                    errorLabel.setText("fail");
                    errorLabel.setTextFill(Color.RED);

                } else {
                    errorLabel.setText(response);
                    errorLabel.setTextFill(Color.RED);
                }


            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);

            }

        });

    }
}
