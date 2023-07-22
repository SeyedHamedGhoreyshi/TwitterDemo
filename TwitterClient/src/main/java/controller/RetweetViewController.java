package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.Retweet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class RetweetViewController implements Initializable {
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
    private Label retweetUserName;

    @FXML
    private Label time;

    @FXML
    private Label tweetTextLabel;

    @FXML
    private Label userName;

    public void setRetweet(Retweet retweet) throws IOException {
        tweetTextLabel.setText(retweet.getTweetText());
        userName.setText(retweet.getUserName());
        String receivedAvatarAsString = retweet.getAvatar();
        byte[] receivedAvatarInBytes = Base64.getDecoder().decode(receivedAvatarAsString);
        ByteArrayInputStream avatarInputStream = new ByteArrayInputStream(receivedAvatarInBytes);
        Image avatar = new Image(avatarInputStream);
        myCircle.setStroke(Color.SEAGREEN);
        myCircle.setFill(new ImagePattern(avatar));
        myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        avatarInputStream.close();
        numLikes.setText(String.valueOf(retweet.getNumLikes()));
        numComments.setText(String.valueOf(retweet.getNumComments()));
        numRetweets.setText(String.valueOf(retweet.getNumRetweets()));
//        tweetIdLabel.setText(retweet.getTweetId());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
