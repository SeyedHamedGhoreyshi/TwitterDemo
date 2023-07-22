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
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.MyClient;
import model.Reply;
import model.Tweet;
import model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class WrietCommentController implements Initializable {
    @FXML
    private Button commentBTN;

    @FXML
    private TextArea commentTextField;

    @FXML
    private Circle myCircle;
    @FXML
    private Label errorLabel;

    @FXML
    private ImageView backImage;
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
        commentBTN.setOnAction(actionEvent -> {
            try {
                if (commentTextField.getText().length() != 0) {
                    StringBuilder token = new StringBuilder();
                    StringBuilder userName = new StringBuilder();
                    StringBuilder tweetId = new StringBuilder();

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

                    File tweetIdFile = new File("tweetId.txt");
                    FileReader tifr = new FileReader(tweetIdFile.getAbsoluteFile());
                    BufferedReader tibr = new BufferedReader(tifr);
                    String tiline;
                    while ((tiline = tibr.readLine()) != null) {
                        tweetId.append(tiline) ;
                    }
                    tibr.close();
                    LocalDateTime now = LocalDateTime.now();
                    String avatar = getAvatar();
                    Reply reply = new Reply(commentTextField.getText() , userName.toString() , now.toString() , token.toString() ,avatar , tweetId.toString());
                    Gson gson = new Gson();
                    String json = gson.toJson(reply);
                    MyClient myClient = new MyClient();
                    String response = myClient.sendRequest(json, "reply");
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
                    errorLabel.setText("enter your comment text");
                    errorLabel.setTextFill(Color.RED);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        backImage.setOnMouseClicked(mouseEvent -> {
            Stage currentStage = (Stage) backImage.getScene().getWindow();
            Stage previousStage = (Stage) currentStage.getOwner();
            currentStage.hide();
            previousStage.show();
        });


    }
}
