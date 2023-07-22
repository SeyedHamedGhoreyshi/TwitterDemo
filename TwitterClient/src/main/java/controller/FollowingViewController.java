package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twitter.twitterclient.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.MyClient;
import model.Tweet;
import model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FollowingViewController implements Initializable {
    @FXML
    private ImageView backImage;

    @FXML
    private ListView<User> followingListView;
    private List<User> users ;
    private void getFollowing() throws IOException {
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
        String response = myClient.sendRequest(userName.toString(), "getFollowings");
        Gson gson = new Gson();
        Type type = new TypeToken<List<User>>() {
        }.getType();
        users = gson.fromJson(response, type);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getFollowing() ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObservableList<User> userObservableList = FXCollections.observableArrayList();

       userObservableList.addAll(users) ;



        followingListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> listView) {
                return new ListCell<User>() {
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (empty || user == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader userFxmlLoader = new FXMLLoader(HelloApplication.class.getResource("miniProfile.fxml"));
                                AnchorPane tweetItem = userFxmlLoader.load();
                                MiniProfileController miniProfileController = userFxmlLoader.getController() ;
                                miniProfileController.setUser(user);
                                setGraphic(tweetItem);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                };
            }
        });

        followingListView.setItems(userObservableList);
        backImage.setOnMouseClicked(mouseEvent -> {
            Stage currentStage = (Stage) backImage.getScene().getWindow();
            Stage previousStage = (Stage) currentStage.getOwner();
            currentStage.hide();
            previousStage.show();
        });

    }
}
