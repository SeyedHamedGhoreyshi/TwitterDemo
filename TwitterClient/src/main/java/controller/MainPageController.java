package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twitter.twitterclient.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.MyClient;
import model.Retweet;
import model.Tweet;
import model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {


    @FXML
    private ImageView addTweet;
    @FXML
    private ImageView searchField;
    @FXML
    private Button imageButton;
    @FXML
    private ImageView imageView;
    @FXML
    private ListView<Tweet> tweetListView;
    private List<Tweet> tweetsList;

    @FXML
    private Circle myCircle;
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

    private List<Tweet> getTimeLine() throws IOException {
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
        String response = myClient.sendRequest(userName.toString(), "getTimeLine");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Tweet>>() {
        }.getType();
        tweetsList = gson.fromJson(response, type);
        return tweetsList;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            String receivedAvatarAsString = getAvatar() ;
            byte[] receivedAvatarInBytes = Base64.getDecoder().decode(receivedAvatarAsString);
            ByteArrayInputStream avatarInputStream = new ByteArrayInputStream(receivedAvatarInBytes);
            Image avatar = new Image(avatarInputStream);
            myCircle.setStroke(Color.SEAGREEN);
            myCircle.setFill(new ImagePattern(avatar));
            myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            List<Tweet> tweetList = getTimeLine() ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Tweet> tweets = FXCollections.observableArrayList();

        for (Tweet tweet1:tweetsList) {
            tweets.add(tweet1) ;

        }



        tweetListView.setCellFactory(new Callback<ListView<Tweet>, ListCell<Tweet>>() {
            @Override
            public ListCell<Tweet> call(ListView<Tweet> listView) {
                return new ListCell<Tweet>() {
                    protected void updateItem(Tweet tweet, boolean empty) {
                        super.updateItem(tweet, empty);
                        if (empty || tweet == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            try {

                                if (tweet instanceof Retweet){
                                    FXMLLoader tweetFxmlLoader = new FXMLLoader(HelloApplication.class.getResource("RetweetView.fxml"));
                                    AnchorPane tweetItem = tweetFxmlLoader.load();
                                    TweetController tweetController = tweetFxmlLoader.getController();
                                    tweetController.setTweet(tweet);
                                    setGraphic(tweetItem);

                                }
                                else if (tweet instanceof Tweet) {
                                    FXMLLoader tweetFxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tweet.fxml"));
                                    AnchorPane tweetItem = tweetFxmlLoader.load();
                                    TweetController tweetController = tweetFxmlLoader.getController();
                                    tweetController.setTweet(tweet);
                                    setGraphic(tweetItem);

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                };
            }
        });

        tweetListView.setItems(tweets);

        addTweet.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TweetPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) addTweet.getScene().getWindow();
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        myCircle.setOnMouseClicked(mouseEvent ->

        {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.setScene(scene);
            Stage currentStage = (Stage) myCircle.getScene().getWindow();
            currentStage.hide();
            stage.show();

        });
        searchField.setOnMouseClicked(mouseEvent ->

        {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Search.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.setScene(scene);
            Stage currentStage = (Stage) myCircle.getScene().getWindow();
            currentStage.hide();
            stage.show();

        });

    }


}