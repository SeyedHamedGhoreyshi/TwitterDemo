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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.MyClient;
import model.Tweet;
import model.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    @FXML
    private ImageView mainPageField;

    @FXML
    private ImageView tweetPageField;



    @FXML
    private TextField searchField;

    @FXML
    private ListView<Tweet> tweetListView;

    @FXML
    private ListView<User> userListView;


    @FXML
    private ImageView searchImage;

    private List<Tweet> tweetSearchLists ;
    private List<User> userSearchLists ;



    private List<?> search(){
        MyClient myClient = new MyClient() ;
        String response = myClient.sendRequest(searchField.getText() , "search") ;
        Gson gson = new Gson();
        if (searchField.getText().charAt(0) == '#') {
            Type type = new TypeToken<List<Tweet>>() {
            }.getType();
           tweetSearchLists = gson.fromJson(response, type);
            return tweetSearchLists;
        }
        else {
            Type type = new TypeToken<List<User>>() {
            }.getType();
            userSearchLists = gson.fromJson(response, type);
            return userSearchLists;
        }


    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     searchImage.setOnMouseClicked(mouseEvent -> {
         search() ;
         String searchTerm = searchField.getText().trim();
       if (searchTerm.startsWith("#")){
           userListView.getItems().clear();
           ObservableList<Tweet> tweets = FXCollections.observableArrayList();
           tweets.addAll(tweetSearchLists);
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
                                   // Load the TweetItem
                                   FXMLLoader tweetFxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tweet.fxml"));
                                   AnchorPane tweetItem = tweetFxmlLoader.load();
                                   TweetController tweetController = tweetFxmlLoader.getController();
                                   tweetController.setTweet(tweet);
                                   setGraphic(tweetItem);
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }


                           }
                       }
                   };
               }
           });

           tweetListView.setItems(tweets);

       }
       else {
           tweetListView.getItems().clear();

           ObservableList<User> users = FXCollections.observableArrayList();

          users.addAll(userSearchLists) ;



           userListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
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
                                   // Load the TweetItem
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

           userListView.setItems(users);

       }
     });




        mainPageField.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) mainPageField.getScene().getWindow();
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tweetPageField.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TweetPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                Stage currentStage = (Stage) mainPageField.getScene().getWindow();
                currentStage.hide();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
