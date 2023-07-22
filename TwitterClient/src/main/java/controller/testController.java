package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class testController {
    @FXML
    private ImageView profilePicImageView;

    @FXML
    private Button selectImageButton;
    @FXML
    private void selectImage() {
        // Create a FileChooser dialog to allow the user to select an image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        // Get the Stage from the Button and show the FileChooser dialog
        File file = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (file != null) {
            // Create an Image object from the selected file
            Image image = new Image(((File) file).toURI().toString());

            // Set the Image as the source for the ImageView
            profilePicImageView.setImage(image);
        }
    }
}
