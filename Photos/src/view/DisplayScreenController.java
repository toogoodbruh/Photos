package view;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import app.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Controller class to handle the functionality of displaying a picture
 */
public class DisplayScreenController {
    @FXML
    private Button BackButton;
    @FXML
    private TextField Date;
    @FXML
    private TextField Caption;
    @FXML
    private ListView tagsListView;
    @FXML
    private ImageView displayImageView;
    @FXML
    private Button LogOutButton;
    @FXML
    private Button QuitButton;

    private Album currentAlbum;
    private Photo currPhoto;
    private User currentUser;

    /**
     * start method to be called to generate all tags, captions, dates that will be displayed along with the chosen photo
     * @param stage
     * @param currUser
     * @param currAlbum
     * @param selectedAlbumPhoto
     * @throws FileNotFoundException
     */
    public void start(Stage stage, User currUser, Album currAlbum, Photo selectedAlbumPhoto) throws FileNotFoundException {
        this.currPhoto = selectedAlbumPhoto;
        this.currentAlbum = currAlbum;
        this.currentUser = currUser;
        displayImageView.setImage(selectedAlbumPhoto.getImage());
        Caption.setText(selectedAlbumPhoto.getCaption());
        Date.setText(String.valueOf(selectedAlbumPhoto.getDate()));

        if (selectedAlbumPhoto.getTagList() != null){
            ObservableList<Tag> tagObservableList = FXCollections.observableArrayList(selectedAlbumPhoto.getTagList());
            tagsListView.setItems(tagObservableList);
            tagsListView.setCellFactory(param -> new ListCell<Tag>(){
                protected void updateItem(Tag tag, boolean isEmpty){
                    super.updateItem(tag, isEmpty);
                    if (tag == null || tag.getTagName() == null || isEmpty) setText(null);
                    else setText(tag.getTagName()+":"+tag.getTagValue());
                }
            });

        }
    }

    /**
     * method to exit application safely
     * @param actionEvent
     */
    public void QuitHandler(ActionEvent actionEvent) {
        System.exit(1);
    }

    /**
     * method to log out of current user interface
     * @param actionEvent
     * @throws IOException
     */
    public void LogOutHandler(ActionEvent actionEvent) throws IOException {
        Stage stage=Photos.getStage();
        FXMLLoader loader = new FXMLLoader();
        AnchorPane root=loader.load(getClass().getResource("UserLogin.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * method to go back to album interface screen
     * @param actionEvent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void backHandler(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Stage stage=Photos.getStage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumInterface.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        AlbumInterfaceController cont1 = loader.getController();
        cont1.start(stage, this.currentUser, this.currentAlbum.getAlbumName());
        stage.setScene(new Scene(root));
        stage.show();
    }
}
