package controller;

import com.google.gson.Gson;
import com.twitter.twitterclient.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import model.MyClient;
import model.Tweet;
import model.User;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.ResourceBundle;

public class TweetPageController implements Initializable {
    @FXML
    private ImageView mainPageField;

    @FXML
    private ImageView searchField;

    @FXML
    private Button tweetButton;
    @FXML
    private Label errorLable;
    @FXML
    private TextArea tweetTextField;

    private String getAvatar() throws IOException {
        StringBuilder userName = new StringBuilder();
        File userNamefile = new File("userName.txt");
        FileReader fru = new FileReader(userNamefile.getAbsoluteFile());
        BufferedReader bru = new BufferedReader(fru);
        String userLine;
        while ((userLine = bru.readLine()) != null) {
            userName.append(userLine);
        }
        bru.close();
        MyClient myClient = new MyClient();
        String response = myClient.sendRequest(userName.toString(), "getUser");
        Gson gson = new Gson();
        User user = gson.fromJson(response, User.class);
        String receivedAvatarAsString = user.getAvatar();
        return receivedAvatarAsString;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPageField.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) mainPageField.getScene().getWindow();
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        searchField.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Search.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) searchField.getScene().getWindow();
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tweetButton.setOnAction(actionEvent -> {
            try {


                if (tweetTextField.getText().length() != 0) {
                    StringBuilder token = new StringBuilder();
                    StringBuilder userName = new StringBuilder();

                    File tokenFile = new File("token.txt");
                    FileReader tfr = new FileReader(tokenFile.getAbsoluteFile());
                    BufferedReader tbr = new BufferedReader(tfr);
                    String tline;
                    while ((tline = tbr.readLine()) != null) {
                        token.append(tline);
                    }
                    tbr.close();


                    File userNameFile = new File("userName.txt");
                    FileReader ufr = new FileReader(userNameFile.getAbsoluteFile());
                    BufferedReader ubr = new BufferedReader(ufr);
                    String uline;
                    while ((uline = ubr.readLine()) != null) {
                        userName.append(uline);
                    }
                    ubr.close();
                    LocalDateTime now = LocalDateTime.now();
                    String avatar = getAvatar();
                    Tweet tweet = new Tweet(tweetTextField.getText(), userName.toString(), now.toString(), token.toString(), avatar);
                    Gson gson = new Gson();
                    String json = gson.toJson(tweet);
                    MyClient myClient = new MyClient();
                    String response = myClient.sendRequest(json, "tweet");
                    if (response.equals("token is invalid")) {
                        errorLable.setText("token is invalid");
                        errorLable.setTextFill(Color.RED);


                    } else if (response.equals("success")) {
                        errorLable.setText("success");
                        errorLable.setTextFill(Color.GREEN);
                    } else if (response.equals("fail")) {
                        errorLable.setText("fail");
                        errorLable.setTextFill(Color.RED);

                    } else {
                        errorLable.setText(response);
                        errorLable.setTextFill(Color.RED);
                    }
                } else {
                    errorLable.setText("enter your tweet text");
                    errorLable.setTextFill(Color.RED);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

    }
}
