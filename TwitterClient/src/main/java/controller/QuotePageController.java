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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.MyClient;
import model.Quote;
import model.Tweet;
import model.User;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.ResourceBundle;

public class QuotePageController implements Initializable {
    @FXML
    private ImageView backImage;

    @FXML
    private Circle myCircle;

    @FXML
    private TextArea queteTextField;

    @FXML
    private Circle tweetProfile;

    @FXML
    private Label tweetText;

    @FXML
    private Label tweetTime;

    @FXML
    private Label tweetUserName;
    @FXML
    private Button tweetBTN;
    @FXML
    private Label errorLabel;
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

    private void getUser() throws IOException {
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
        byte[] receivedAvatarInBytes = Base64.getDecoder().decode(receivedAvatarAsString);
        ByteArrayInputStream avatarInputStream = new ByteArrayInputStream(receivedAvatarInBytes);
        Image avatar = new Image(avatarInputStream);
        myCircle.setStroke(Color.SEAGREEN);
        myCircle.setFill(new ImagePattern(avatar));
        myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        avatarInputStream.close();

    }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getUser();
            Tweet tweet = getTweet();
            tweetUserName.setText(tweet.getUserName());
            tweetText.setText(tweet.getTweetText());
            tweetTime.setText(tweet.getDate());
            String receivedAvatarAsString = tweet.getAvatar();
            byte[] receivedAvatarInBytes = Base64.getDecoder().decode(receivedAvatarAsString);
            ByteArrayInputStream avatarInputStream = new ByteArrayInputStream(receivedAvatarInBytes);
            Image avatar = new Image(avatarInputStream);
            tweetProfile.setStroke(Color.SEAGREEN);
            tweetProfile.setFill(new ImagePattern(avatar));
            tweetProfile.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            avatarInputStream.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        backImage.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) backImage.getScene().getWindow();
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tweetBTN.setOnAction(actionEvent -> {
            try {
                if (queteTextField.getText().length() != 0) {
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

                    StringBuilder tweetId = new StringBuilder();
                    File tweetIdFile = new File("tweetId.txt");
                    FileReader tifr = new FileReader(tweetIdFile.getAbsoluteFile());
                    BufferedReader tibr = new BufferedReader(tifr);
                    String tiline;
                    while ((tiline = tibr.readLine()) != null) {
                        tweetId.append(tiline);
                    }
                    tibr.close();

                    LocalDateTime now = LocalDateTime.now();
                    String avatar = getAvatar();
                    Quote quote = new Quote(queteTextField.getText() ,userName.toString() , now.toString() ,token.toString() ,avatar ,tweetId.toString()) ;
                    Gson gson = new Gson();
                    String json = gson.toJson(quote);
                    MyClient myClient = new MyClient();
                    String response = myClient.sendRequest(json, "quote");
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
                } else {
                    errorLabel.setText("enter your tweet text");
                    errorLabel.setTextFill(Color.RED);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }
}
