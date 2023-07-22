package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.twitter.twitterclient.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Login;
import model.LoginToken;
import model.MyClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginPageController implements Initializable {
    @FXML
    private Button loginBTN;

    @FXML
    private PasswordField passWordField;

    @FXML
    private Button signUpBTN;

    @FXML
    private TextField userNameTextField;
    @FXML
    private Label errorLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginBTN.setOnAction(actionEvent -> {
            String userId = userNameTextField.getText();
            String passWord = passWordField.getText();
            if (userId.length()!= 0  && passWord.length() != 0) {

                Login login = new Login(userId, passWord);
                Gson gson = new Gson();
                String json = gson.toJson(login);
                MyClient myClient = new MyClient();
                String receiveJson = myClient.sendRequest(json, "login");
                LoginToken loginToken = gson.fromJson(receiveJson , LoginToken.class) ;
                String response = loginToken.getRespone() ;
                if (response.equals("success")) {
                    try {
                        File file = new File( "token.txt");
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(loginToken.getToken());
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        File file = new File( "userName.txt");
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(userId);
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainPage.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    currentStage.hide();
                    stage.show();

                } else if (response.equals("fail")) {
                    errorLabel.setText("login error");
                    errorLabel.setTextFill(Color.RED);


                } else {
                    errorLabel.setText("Error");
                    errorLabel.setTextFill(Color.RED);

                }
            } else {
                errorLabel.setText("enter your information");
                errorLabel.setTextFill(Color.RED);
            }

        });
        signUpBTN.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signUp.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.setScene(scene);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.hide();
            stage.show();
        });
    }
}