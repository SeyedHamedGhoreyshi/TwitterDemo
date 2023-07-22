package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.Reply;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class CommentViewController implements Initializable {
    @FXML
    private HBox HBoxForOpenComment;
    @FXML
    private Circle myCircle;
    @FXML
    private Label tweetTextLabel;
    @FXML
    private Label tweetTime;
    @FXML
    private Label userNameTextLabel;
    public void setComment(Reply reply) throws IOException {
        tweetTextLabel.setText(reply.getReplyText());
        userNameTextLabel.setText(reply.getUserName());
        String receivedAvatarAsString = reply.getProfile() ;
        byte[] receivedAvatarInBytes = Base64.getDecoder().decode(receivedAvatarAsString);
        ByteArrayInputStream avatarInputStream = new ByteArrayInputStream(receivedAvatarInBytes);
        Image avatar = new Image(avatarInputStream);
        myCircle.setStroke(Color.SEAGREEN);
        myCircle.setFill(new ImagePattern(avatar));
        myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        avatarInputStream.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
