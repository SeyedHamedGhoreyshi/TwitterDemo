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
import model.Reply;
import model.Tweet;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CommentPageController implements Initializable {
    @FXML
    private ImageView backImage;

    @FXML
    private ListView<Reply> commentsListView;
    private List<Reply> repliesList ;
    private List<Reply> getComments() throws IOException {
        StringBuilder tweetId = new StringBuilder() ;
        File tweetIdFile = new File("tweetId.txt");
        FileReader tifr = new FileReader(tweetIdFile.getAbsoluteFile());
        BufferedReader tibr = new BufferedReader(tifr);
        String tiline;
        while ((tiline = tibr.readLine()) != null) {
            tweetId.append(tiline) ;
        }
        tibr.close();


        MyClient myClient = new MyClient() ;
        String response = myClient.sendRequest(tweetId.toString(), "getComments");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Reply>>() {
        }.getType();
        repliesList = gson.fromJson(response, type);
        return repliesList;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Reply> replies= getComments()  ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Reply> replies1 = FXCollections.observableArrayList();

        for (Reply reply:repliesList) {
            replies1.add(reply) ;
        }



        commentsListView.setCellFactory(new Callback<ListView<Reply>, ListCell<Reply>>() {


            @Override
            public ListCell<Reply> call(ListView<Reply> listView) {
                return new ListCell<Reply>() {
                    protected void updateItem(Reply reply, boolean empty) {
                        super.updateItem(reply, empty);
                        if (empty || reply == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            try {
                                // Load the TweetItem
                                FXMLLoader commentFxmlLoader = new FXMLLoader(HelloApplication.class.getResource("commentView.fxml"));
                                AnchorPane commentItem = commentFxmlLoader.load();
                                CommentViewController commentViewController = commentFxmlLoader.getController();
                                commentViewController.setComment(reply);
                                setGraphic(commentItem);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                };
            }
        });

        commentsListView.setItems(replies1);

        backImage.setOnMouseClicked(mouseEvent -> {
            Stage currentStage = (Stage) backImage.getScene().getWindow();
            Stage previousStage = (Stage) currentStage.getOwner();
            currentStage.hide();
            previousStage.show();
        });
    }
}
