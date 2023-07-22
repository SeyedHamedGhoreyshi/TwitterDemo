package controller;

import com.twitter.twitterclient.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import model.Tweet;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class TweetController implements Initializable {


    @FXML
    private HBox HBoxForOpenComment;

    @FXML
    private ImageView commentIcon;

    @FXML
    private Circle myCircle;

    @FXML
    private Label numComments;

    @FXML
    private Label numLikes;

    @FXML
    private Label numRetweets;

    @FXML
    private ImageView retweetButtom;

    @FXML
    private Label tweetTextLabel;
    @FXML
    private Label tweetTime;
    @FXML
    private Label userNameTextLabel;
    @FXML
    private Label tweetIdLabel;

    public void setTweet(Tweet tweet) throws IOException {
        tweetTextLabel.setText(tweet.getTweetText());
        userNameTextLabel.setText(tweet.getUserName());
        String receivedAvatarAsString = tweet.getAvatar();
        byte[] receivedAvatarInBytes = Base64.getDecoder().decode(receivedAvatarAsString);
        ByteArrayInputStream avatarInputStream = new ByteArrayInputStream(receivedAvatarInBytes);
        Image avatar = new Image(avatarInputStream);
        myCircle.setStroke(Color.SEAGREEN);
        myCircle.setFill(new ImagePattern(avatar));
        myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        avatarInputStream.close();
        numLikes.setText(String.valueOf(tweet.getNumLikes()));
        numComments.setText(String.valueOf(tweet.getNumComments()));
        numRetweets.setText(String.valueOf(tweet.getNumRetweets()));
        tweetIdLabel.setText(tweet.getTweetId());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        retweetButtom.setOnMouseClicked(mouseEvent -> {
            try {
                File file = new File("tweetId.txt");
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(tweetIdLabel.getText());
                bw.close();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Retweet.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) retweetButtom.getScene().getWindow();
                stage.initOwner(currentStage);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        HBoxForOpenComment.setOnMouseClicked(mouseEvent -> {
            try {
                File file = new File("tweetId.txt");
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(tweetIdLabel.getText());
                bw.close();


                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("commentPage.fxml"));
                Scene scene = null;
                scene = new Scene(fxmlLoader.load());

                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) retweetButtom.getScene().getWindow();
                stage.initOwner(currentStage);
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        commentIcon.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("wrietComment.fxml"));
                Scene scene = null;

                scene = new Scene(fxmlLoader.load());

                File file = new File("tweetId.txt");
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(tweetIdLabel.getText());
                bw.close();
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) commentIcon.getScene().getWindow();
                stage.initOwner(currentStage);
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        myCircle.setOnMouseClicked(mouseEvent -> {
            try {


                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("userProfile.fxml"));
                Scene scene = null;
                scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) myCircle.getScene().getWindow();
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    public void initialize(MouseEvent mouseEvent) {
    }
}
