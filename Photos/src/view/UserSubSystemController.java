package view;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import app.*;
import view.UserListController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is a controller class for the user subsystem
 */
public class UserSubSystemController {

    @FXML
    private Button Quit;
    @FXML
    private Button LogOutButton;
    @FXML
    private Button CreateAlbumButton;
    @FXML
    private Button RenameAlbumButton;
    @FXML
    private Button DeleteAlbumButton;
    @FXML
    private Button OpenAlbumButton;

    @FXML
    private TextField albumName;
    @FXML
    private ListView<Album> albumsListView;

    ArrayList<User> users;
    ArrayList<Album> albums;
    ObservableList<Album> observableList;
    private User currUser;


    /**
     * This method displays the user subsystem, and generates a list of all the albums to display as well
     * @param stage
     * @param currUser
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void start(Stage stage, User currUser) throws IOException, ClassNotFoundException {
        this.currUser = currUser;
        this.users = UserListController.read();
        for (User user : users){
            if (user.getUsername().equals(currUser.getUsername())) this.albums = user.getAlbums();
        }

        if (this.albums == null) this.albums = new ArrayList<>();
        this.albumName.setText(null);
        this.observableList = FXCollections.observableArrayList(albums);
        this.albumsListView.setItems(this.observableList);
        this.albumsListView.setCellFactory(param -> new ListCell<Album>(){
            protected void updateItem(Album album, boolean isEmpty){
                super.updateItem(album, isEmpty);
                if (album==null || album.getAlbumName()==null||isEmpty) setText(null);
                else{
                    String albumName = album.getAlbumName();
                    int numPhotos = album.getPhotoList().size();
                    Date[] albumDates = rangeOfDates(album);
                    setText(albumName + "(" + numPhotos + ")" + "        " + albumDates[0] +"-"+ albumDates[1]);
                }
            }
        });

    }

    /**
     * This is a helper method to return the earliest and latest dates photos of an album were added
     * @param album
     * @return
     */
    public Date[] rangeOfDates(Album album){
        Date leastRecent = null;
        Date mostRecent = null;

        for (Photo photo : album.getPhotoList()){
            Date currDate = photo.getDate();
            if (leastRecent == null || currDate.compareTo(leastRecent) < 0) leastRecent = currDate;
            if (mostRecent == null || currDate.compareTo(mostRecent) > 0) mostRecent = currDate;
        }
        return new Date[]{leastRecent, mostRecent};
    }

    /**
     * This is a method to terminate the program
     * @param actionEvent
     */
    public void quitHandler(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * This is a method to log out of the current user and return to the login screen
     * @param actionEvent
     * @throws IOException
     */
    public void logOutHandler(ActionEvent actionEvent) throws IOException {
        Stage stage = Photos.getStage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserLogin.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * This is a method to create an album and update the album list for the current user
     * @param actionEvent
     * @throws IOException
     */
    public void createAlbumHandler(ActionEvent actionEvent) throws IOException {

        if (albumName.getText() == null) {
            UserListController.showAlert("Text empty", "No album entered", "Please enter a name for the album");
            return;
        }
        String newAlbumName = albumName.getText().trim();
        if (doesAlbumExist(newAlbumName)){
            UserListController.showAlert("Album already present", "User has already added this album", "Please enter an album that does not exist yet");
            return;
        }

        this.observableList.add(new Album(newAlbumName));
        this.albumsListView.setItems(observableList);
        this.albumsListView.refresh();
        for (User user : this.users){
            if (user.getUsername().equals(this.currUser.getUsername())) user.addAlbum(new Album(newAlbumName));
        }
        this.currUser.getAlbums().add(new Album(newAlbumName));

        UserListController.write(this.users);

    }

    /**
     * This is a method to rename an album and update the album list for the current user
     * @param actionEvent
     * @throws IOException
     */
    public void renameAlbumHandler(ActionEvent actionEvent) throws IOException {
        int index = this.albumsListView.getSelectionModel().getSelectedIndex();
        String newAlbumName = this.albumName.getText();
        if (newAlbumName.isEmpty()) {
            UserListController.showAlert("Error", "No name entered", "Add a name for the album");
            return;

        } else if (doesAlbumExist(newAlbumName)) {
            UserListController.showAlert("Error", "Album name already exists", "Enter an album name that does not exist");
            return;
        }

        this.observableList.get(index).setAlbumName(newAlbumName);
        this.albums.get(index).setAlbumName(newAlbumName);
        this.albumsListView.setItems(this.observableList);
        this.albumsListView.refresh();

        for (User user : this.users){
            if (user.getUsername().equals(this.currUser.getUsername())) user.setAlbums(this.albums);
        }
        this.currUser.setAlbums(this.albums);
        UserListController.write(this.users);


    }

    /**
     * This is a method to open an album and call the start method of the album interface controller, as well as load a new screen
     * @param actionEvent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void openAlbumHandler(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Stage stage = Photos.getStage();
        Album selectedAlbum = this.albumsListView.getSelectionModel().getSelectedItem();

        if (selectedAlbum == null) {
            UserListController.showAlert("Error", " No Album selected", "Please select an album");
            return;
        }
        String albumName = selectedAlbum.getAlbumName();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumInterface.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        AlbumInterfaceController controller = loader.getController();
        controller.start(stage, currUser, albumName);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * This is a method to delete an album and update the album list for the current user
     * @param actionEvent
     * @throws IOException
     */
    public void deleteAlbumHandler(ActionEvent actionEvent) throws IOException {
        int index = this.albumsListView.getSelectionModel().getSelectedIndex();
        if (this.albumsListView.getSelectionModel().getSelectedItem() == null) {
            UserListController.showAlert("Error", "No album selected", "Please select an album you wish to delete");
            return;
        }

        this.observableList.remove(index);
        this.albums.remove(index);

        this.albumsListView.setItems(this.observableList);

        for (User user : users){
            if (user.getUsername().equals(this.currUser.getUsername())) user.setAlbums(this.albums);
        }
        this.currUser.setAlbums(this.albums);

        UserListController.write(this.users);
    }

    /**
     * This is a method to check if a user album list contains a certain album
     * @param albumName
     * @return
     */
    public boolean doesAlbumExist(String albumName){
        for (int i = 0; i < this.albums.size(); i++) {
            if (this.albums.get(i).getAlbumName().equals(albumName)) {
                return true;
            }
        }
        return false;
    }
}
