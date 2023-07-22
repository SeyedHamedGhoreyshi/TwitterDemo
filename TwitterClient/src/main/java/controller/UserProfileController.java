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
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Block;
import model.Follow;
import model.MyClient;
import model.User;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    @FXML
    private Label bioLabel;

    @FXML
    private Button blockBTN;

    @FXML
    private Label dateLabel;

    @FXML
    private Button followBTN;

    @FXML
    private Label followerNums;

    @FXML
    private Label followingNums;

    @FXML
    private Label locationLabel;

    @FXML
    private Circle myCircle;

    @FXML
    private Label nameLabel;

    @FXML
    private Rectangle rectangleHeader;

    @FXML
    private Label userNameLabel;

    @FXML
    private ImageView backBTN;
    @FXML
    private Label followLabelError;
    @FXML
    private Label blockLabelError;

    @FXML
    private Label followingLabel;
    @FXML
    private Label followerLabel;

    private void getUser() throws IOException {
        StringBuilder userName = new StringBuilder();
        File userNamefile = new File("userName2.txt");
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
        locationLabel.setText(user.getLocation());
        dateLabel.setText(user.getBirthDate());
        followingNums.setText(String.valueOf(user.getFollowings()));
        followerNums.setText(String.valueOf(user.getFollowers()));
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
        MyClient myClient1 = new MyClient();
        Follow follow = new Follow(userName2.toString(), userName.toString(), token.toString());
        Gson gson1 = new Gson();
        String json = gson1.toJson(follow);
        String response1 = myClient1.sendRequest(json, "followCheck");
        if (response1.equals("yes")) {
            followBTN.setText("followed");
            followBTN.setTextFill(Color.SEAGREEN);


        } else if (response1.equals("no")) {
            followBTN.setText("Follow");
        } else {
            followLabelError.setText("invalid token");
            followLabelError.setTextFill(Color.RED);
        }

        MyClient myClient3 = new MyClient();
        Block block1 = new Block(userName2.toString(), userName.toString(), token.toString());
        Gson gson3 = new Gson();
        String json2 = gson3.toJson(block1);
        String response3 = myClient3.sendRequest(json2, "checkBlock");
        if (response3.equals("yes")) {
            blockBTN.setText("Blocked");
            blockBTN.setTextFill(Color.SEAGREEN);
        } else {
            blockBTN.setText("Block");

        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
        getUser();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        backBTN.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) backBTN.getScene().getWindow();
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        followBTN.setOnAction(actionEvent -> {
            if (followBTN.getText().equals("Follow")) {
                try {
                    StringBuilder token = new StringBuilder();
                    StringBuilder userName = new StringBuilder();
                    StringBuilder userName2 = new StringBuilder();
                    File userNamefile2 = new File("userName2.txt");
                    FileReader fru2 = new FileReader(userNamefile2.getAbsoluteFile());
                    BufferedReader bru2 = new BufferedReader(fru2);
                    String userLine2;
                    while ((userLine2 = bru2.readLine()) != null) {
                        userName2.append(userLine2);
                    }
                    bru2.close();

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
                    Follow follow = new Follow(userName.toString(), userName2.toString(), token.toString());
                    MyClient myClient = new MyClient();
                    Gson gson = new Gson();
                    String json = gson.toJson(follow);
                    String response = myClient.sendRequest(json, "follow");
                    if (response.equals("success")) {
                        followBTN.setText("followed");
                        followBTN.setTextFill(Color.SEAGREEN);
                        getUser();

                    } else if (response.equals("fail")) {
                        followLabelError.setText("fail");
                        followLabelError.setTextFill(Color.RED);
                    } else {
                        followLabelError.setText("invalid token");
                        followLabelError.setTextFill(Color.RED);

                    }


                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (followBTN.getText().equals("followed")) {
                try {
                    StringBuilder token = new StringBuilder();
                    StringBuilder userName = new StringBuilder();
                    StringBuilder userName2 = new StringBuilder();
                    File userNamefile2 = new File("userName2.txt");
                    FileReader fru2 = new FileReader(userNamefile2.getAbsoluteFile());
                    BufferedReader bru2 = new BufferedReader(fru2);
                    String userLine2;
                    while ((userLine2 = bru2.readLine()) != null) {
                        userName2.append(userLine2);
                    }
                    bru2.close();

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
                    Follow follow = new Follow(userName.toString(), userName2.toString(), token.toString());
                    MyClient myClient = new MyClient();
                    Gson gson = new Gson();
                    String json = gson.toJson(follow);
                    String response = myClient.sendRequest(json, "unFollow");
                    if (response.equals("success")) {
                        followBTN.setText("Follow");
                        getUser();


                    } else if (response.equals("fail")) {
                        followLabelError.setText("fail");
                        followLabelError.setTextFill(Color.RED);
                    } else {
                        followLabelError.setText("invalid token");
                        followLabelError.setTextFill(Color.RED);

                    }


                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });
        followingLabel.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("followingPage.fxml"));

                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) followingLabel.getScene().getWindow();
                stage.initOwner(currentStage);
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        followerLabel.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("followerPage.fxml"));

                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) followingLabel.getScene().getWindow();
                stage.initOwner(currentStage);
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        blockBTN.setOnAction(actionEvent -> {
            if (blockBTN.getText().equals("Block")) {
                try {
                    StringBuilder token = new StringBuilder();
                    StringBuilder userName = new StringBuilder();
                    StringBuilder userName2 = new StringBuilder();
                    File userNamefile2 = new File("userName2.txt");
                    FileReader fru2 = new FileReader(userNamefile2.getAbsoluteFile());
                    BufferedReader bru2 = new BufferedReader(fru2);
                    String userLine2;
                    while ((userLine2 = bru2.readLine()) != null) {
                        userName2.append(userLine2);
                    }
                    bru2.close();

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
                    Block block = new Block(userName.toString(), userName2.toString(), token.toString());
                    MyClient myClient = new MyClient();
                    Gson gson = new Gson();
                    String json = gson.toJson(block);
                    String response = myClient.sendRequest(json, "block");
                    if (response.equals("success")) {
                        blockBTN.setText("Blocked");
                        blockBTN.setTextFill(Color.SEAGREEN);
                        getUser();

                    } else if (response.equals("fail")) {
                        blockLabelError.setText("fail");
                        blockLabelError.setTextFill(Color.RED);
                    } else {
                        blockLabelError.setText("invalid token");
                        blockLabelError.setTextFill(Color.RED);

                    }


                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (blockBTN.getText().equals("Blocked")) {
                try {
                    StringBuilder token = new StringBuilder();
                    StringBuilder userName = new StringBuilder();
                    StringBuilder userName2 = new StringBuilder();
                    File userNamefile2 = new File("userName2.txt");
                    FileReader fru2 = new FileReader(userNamefile2.getAbsoluteFile());
                    BufferedReader bru2 = new BufferedReader(fru2);
                    String userLine2;
                    while ((userLine2 = bru2.readLine()) != null) {
                        userName2.append(userLine2);
                    }
                    bru2.close();

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
                    Block block = new Block(userName.toString(), userName2.toString(), token.toString());
                    MyClient myClient = new MyClient();
                    Gson gson = new Gson();
                    String json = gson.toJson(block);
                    String response = myClient.sendRequest(json, "unBlock");
                    if (response.equals("success")) {
                        blockBTN.setText("Block");
                        getUser();


                    } else if (response.equals("fail")) {
                        blockLabelError.setText("fail");
                        blockLabelError.setTextFill(Color.RED);
                    } else {
                        blockLabelError.setText("invalid token");
                        blockLabelError.setTextFill(Color.RED);

                    }


                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }

        });

    }
}
