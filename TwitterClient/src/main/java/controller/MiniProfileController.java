package controller;

import com.google.gson.Gson;
import com.twitter.twitterclient.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Block;
import model.MyClient;
import model.User;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class MiniProfileController implements Initializable {
    @FXML
    private Label bioLabel;

    @FXML
    private Circle myCircle;

    @FXML
    private Label userNameLabel;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox vbox;

    public void setUser(User user) throws IOException {
        bioLabel.setText(user.getBio());
        userNameLabel.setText(user.getUserName());
        String receivedAvatarAsString = user.getAvatar();
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
        vbox.setOnMouseClicked(mouseEvent -> {
            try {
                String userId = userNameLabel.getText();
                File file = new File("userName2.txt");
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(userId);
                bw.close();

                StringBuilder userName2 = new StringBuilder();
                File userNamefile2 = new File("userName.txt");
                FileReader fru2 = new FileReader(userNamefile2.getAbsoluteFile());
                BufferedReader bru2 = new BufferedReader(fru2);
                String userLine2;
                while ((userLine2 = bru2.readLine()) != null) {
                    userName2.append(userLine2);
                }
                bru2.close();
                StringBuilder token = new StringBuilder();
                File tokenFile = new File("token.txt");
                FileReader frt = new FileReader(tokenFile.getAbsoluteFile());
                BufferedReader brt = new BufferedReader(frt);
                String tokenLine;
                while ((tokenLine = brt.readLine()) != null) {
                    token.append(tokenLine);
                }
                brt.close();
                MyClient myClient2 = new MyClient();
                Block block = new Block(userNameLabel.getText(), userName2.toString(), token.toString());
                Gson gson2 = new Gson();
                String json1 = gson2.toJson(block);
                String response2 = myClient2.sendRequest(json1, "checkBlock");
                if (response2.equals("yes")){
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("blockPage.fxml"));
                    Scene scene = null;
                    scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    Stage currentStage = (Stage) anchorPane.getScene().getWindow();
                    currentStage.hide();
                    stage.show();

                }
                else {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("userProfile.fxml"));
                    Scene scene = null;
                    scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    Stage currentStage = (Stage) anchorPane.getScene().getWindow();
                    currentStage.hide();
                    stage.show();

                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });


    }
}
