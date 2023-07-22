package controller;

import com.google.gson.Gson;
import com.twitter.twitterclient.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.MyClient;
import model.User;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class addBioController implements Initializable {
    @FXML
    private Button addBioButton;

    @FXML
    private ImageView backImage;

    @FXML
    private TextField bioTextField;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBioButton.setOnAction(actionEvent -> {
            if (bioTextField.getText().length()!=0){
                StringBuilder token = new StringBuilder();
                StringBuilder userName = new StringBuilder();
                try {
                    File tokenFile = new File("token.txt");
                    FileReader frt = new FileReader(tokenFile.getAbsoluteFile());
                    BufferedReader brt = new BufferedReader(frt);
                    String tokenLine;
                    while ((tokenLine = brt.readLine()) != null) {
                        token.append(tokenLine);
                    }
                    brt.close();
                    File userNamefile = new File("userName.txt");
                    FileReader fru = new FileReader(userNamefile.getAbsoluteFile());
                    BufferedReader bru = new BufferedReader(fru);
                    String userLine;
                    while ((userLine = bru.readLine()) != null) {
                        userName.append(userLine);
                    }
                    bru.close();
                } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
                User user = new User() ;
                user.setBio(bioTextField.getText());
                user.setToken(token.toString());
                user.setUserName(userName.toString());
                Gson gson = new Gson() ;
                String json = gson.toJson(user);
                MyClient myClient = new MyClient() ;
                String response = myClient.sendRequest(json , "setBio");
                if (response.equals("success")){
                    errorLabel.setText("success");
                    errorLabel.setTextFill(Color.GREEN);
                } else if (response.equals("fail")) {
                    errorLabel.setText("fail");
                    errorLabel.setTextFill(Color.RED);

                } else if (response.equals("invalid token")) {
                    errorLabel.setText("invail token");
                    errorLabel.setTextFill(Color.RED);

                }

            }

            else {
                errorLabel.setText("enter your bio");
                errorLabel.setTextFill(Color.RED);
            }
        });
        backImage.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Profile.fxml"));
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

    }
}
