package controller;

import com.google.gson.Gson;
import com.twitter.twitterclient.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MyClient;
import model.User;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Button addBioButton;

    @FXML
    private ImageView backField;

    @FXML
    private Label bioLabel;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Label locationLbel;

    @FXML
    private Circle myCircle;

    @FXML
    private Label nameLabel;

    @FXML
    private Rectangle rectangleHeader;

    @FXML
    private Button selectHeaderButton;

    @FXML
    private Button selectImageButton;

    @FXML
    private Label userNameLabel;
    @FXML
    private Label followersLabel;

    @FXML
    private Label followingsLabel;

    @FXML
    private Label follower;

    @FXML
    private Label following;


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
        bioLabel.setText(user.getBio());
        userNameLabel.setText(user.getUserName());
        nameLabel.setText(user.getFirstName());
        locationLbel.setText(user.getLocation());
        birthdayLabel.setText(user.getBirthDate());
        followersLabel.setText(String.valueOf(user.getFollowings()));
        followingsLabel.setText(String.valueOf(user.getFollowers()));
        String receivedAvatarAsString = user.getAvatar();
        byte[] receivedAvatarInBytes = Base64.getDecoder().decode(receivedAvatarAsString);
        ByteArrayInputStream avatarInputStream = new ByteArrayInputStream(receivedAvatarInBytes);
        Image avatar = new Image(avatarInputStream);
        myCircle.setStroke(Color.SEAGREEN);
        myCircle.setFill(new ImagePattern(avatar));
        myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));

        String receivedHeaderAsString = user.getHeader();
        byte[] receivedHeaderInBytes = Base64.getDecoder().decode(receivedHeaderAsString);
        ByteArrayInputStream headerInputStream = new ByteArrayInputStream(receivedHeaderInBytes);
        Image header = new Image(headerInputStream);
        rectangleHeader.setStroke(Color.SEAGREEN);
        rectangleHeader.setFill(new ImagePattern(header));
        rectangleHeader.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        avatarInputStream.close();
        headerInputStream.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getUser();
            String userId = userNameLabel.getText();
            File file = new File("userName2.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(userId);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        backField.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) backField.getScene().getWindow();
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        selectImageButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            File imageFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
            Image image = new Image(((File) imageFile).toURI().toString());
            StringBuilder token = new StringBuilder();
            StringBuilder userName = new StringBuilder();
            if (imageFile != null) {
                try {
                    FileInputStream imageFileStream = new FileInputStream(imageFile);
                    byte[] imageInBytes = imageFileStream.readAllBytes();
                    String imageAsString = Base64.getEncoder().encodeToString(imageInBytes);
                    imageFileStream.close();

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

                    User user = new User();
                    user.setAvatar(imageAsString);
                    user.setToken(token.toString());
                    user.setUserName(userName.toString());
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    MyClient myClient = new MyClient();
                    String response = myClient.sendRequest(json, "setAvatar");
                    if (response.equals("success")) {
                        myCircle.setStroke(Color.SEAGREEN);
                        myCircle.setFill(new ImagePattern(image));
                        myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
                    } else {
                        errorLabel.setText("fail");
                        errorLabel.setTextFill(Color.RED);

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }


            }
        });
        selectHeaderButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            File imageFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
            Image image = new Image(((File) imageFile).toURI().toString());
            StringBuilder token = new StringBuilder();
            StringBuilder userName = new StringBuilder();
            if (imageFile != null) {
                try {
                    FileInputStream imageFileStream = null;
                    byte[] imageInBytes = new byte[0];
                    imageFileStream = new FileInputStream(imageFile);
                    imageInBytes = imageFileStream.readAllBytes();
                    String imageAsString = Base64.getEncoder().encodeToString(imageInBytes);
                    imageFileStream.close();

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

                    User user = new User();
                    user.setHeader(imageAsString);
                    user.setToken(token.toString());
                    user.setUserName(userName.toString());
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    MyClient myClient = new MyClient();
                    String response = myClient.sendRequest(json, "setHeader");
                    if (response.equals("success")) {
                        rectangleHeader.setStroke(Color.SEAGREEN);
                        rectangleHeader.setFill(new ImagePattern(image));
                        rectangleHeader.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
                    } else {
                        errorLabel.setText("fail");
                        errorLabel.setTextFill(Color.RED);

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }

        });
        addBioButton.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addBio.fxml"));
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
        following.setOnMouseClicked(mouseEvent -> {
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("followingPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) following.getScene().getWindow();
                stage.initOwner(currentStage);
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        follower.setOnMouseClicked(mouseEvent -> {
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("followerPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) follower.getScene().getWindow();
                stage.initOwner(currentStage);
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}






