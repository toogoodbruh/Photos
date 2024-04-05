package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import app.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * controller class to show the slideshow of pictures in an album
 */
public class SlideShowController {
    @FXML
    private Button QuitButton;
    @FXML
    private Button LogOutButton;
    @FXML
    private Button BackButton;
    @FXML
    private Button PreviousPhotoButton;
    @FXML
    private Button NextPhotoButton;
    @FXML
    private ImageView ImageView;

    private User currentUser;
    private Album currentAlbum;

    private int currentIndex=0;
    private int albumSize=0;

    /**
     * method to generate the slideshow interface
     * @param stage
     * @param currUser
     * @param currAlbum
     * @param i
     * @throws FileNotFoundException
     */
    public void start(Stage stage, User currUser, Album currAlbum, int i) throws FileNotFoundException {
        this.currentUser=currUser;
        this.currentAlbum=currAlbum;
        this.currentIndex=i;
        this.albumSize=this.currentAlbum.getPhotoList().size();
        ImageView.setImage(this.currentAlbum.getPhotoList().get(currentIndex).getImage());
    }

    /**
     * method to exit the application safely
     * @param actionEvent
     */
    public void QuitHandler(ActionEvent actionEvent) {
        System.exit(1);
    }

    /**
     * method to exit out of current user interface
     * @param actionEvent
     * @throws IOException
     */
    public void LogOutHandler(ActionEvent actionEvent) throws IOException {
        Stage stage = Photos.getStage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserLogin.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * method to return to album interface
     * @param actionEvent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void backHandler(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Stage stage = Photos.getStage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumInterface.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        AlbumInterfaceController cont1 = loader.getController();
        cont1.start(stage, this.currentUser, currentAlbum.getAlbumName());

        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * method to go back one photo
     * @param actionEvent
     * @throws FileNotFoundException
     */
    public void previousPhotoHandler(ActionEvent actionEvent) throws FileNotFoundException {
        this.currentIndex--;
        if(this.currentIndex<0) this.currentIndex=this.albumSize-1;
        this.ImageView.setImage(this.currentAlbum.getPhotoList().get(this.currentIndex).getImage());
    }

    /**
     * method to go forward one photo
     * @param actionEvent
     * @throws FileNotFoundException
     */
    public void nextPhotoHandler(ActionEvent actionEvent) throws FileNotFoundException {
        this.currentIndex++;
        if(this.currentIndex>=this.albumSize) this.currentIndex=0;

        this.ImageView.setImage(this.currentAlbum.getPhotoList().get(this.currentIndex).getImage());
    }
}
