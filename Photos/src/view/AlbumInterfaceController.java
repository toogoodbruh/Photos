package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import app.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.util.ArrayList;

/***
 * AlbumInterfaceController for handling different functions operated on a standalone album in the users
 * list of albums.
 * @author Nikhil Munagala nsm123
 * @author Aarav Modi arm360
 */
public class AlbumInterfaceController {
    @FXML
    private Button LogOutButton;
    @FXML
    private Button QuitButton;
    @FXML
    private Button AddPhotoButton;
    @FXML
    private Button DeletePhotoButton;
    @FXML
    private Button AddTagButton;
    @FXML
    private Button DeleteTagButton;
    @FXML
    private Button SlideshowButton;
    @FXML
    private Button DisplayButton;
    @FXML
    private Button MoveFromAlbumButton;
    @FXML
    private Button CopyFromAlbumButton;
    @FXML
    private Button CaptionButton;
    @FXML
    private Button BackButton;

    @FXML
    private ListView<Photo> photoListView;

    ObservableList<Photo> observableList;
    private String tag1;
    private String tagVal1;
    private String tag2;
    private String tagVal2;
    private Album currAlbum;
    ArrayList<User> users;
    private User currUser;

    /***
     * Method that loads up the AlbumInterface for an album in a user's list of albums.
     * @param stage is a window that is used to load up the album interface for the currentUser.
     * @param currUser to set/display the currentAlbum for the currentUser
     * @param albumName
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public void start(Stage stage, User currUser, String albumName) throws IOException, ClassNotFoundException {
        this.users = UserListController.read();
        this.currUser = currUser;
        for (User user : this.users){
            if (user.getUsername().equals(currUser.getUsername())){
                ArrayList<Album> userAlbums = user.getAlbums();
                for (Album album : userAlbums){
                    if (album.getAlbumName().equals(albumName)) this.currAlbum = album;
                }
            }
        }

        this.observableList = FXCollections.observableArrayList(this.currAlbum.getPhotoList());

        this.photoListView.refresh();
        this.photoListView.setItems(this.observableList);
        this.photoListView.setCellFactory(param -> new ListCell<Photo>(){
            private ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Photo photo, boolean isEmpty){
                super.updateItem(photo, isEmpty);

                try{
                    if (photo == null || photo.getImage() == null || isEmpty){
                        setText(null);
                        setGraphic(null);
                    }
                    else{
                        imageView.setImage(photo.getImage());
                        imageView.setFitHeight(100);
                        imageView.setFitWidth(120);
                        setGraphic(imageView);
                        setText(photo.getCaption()+"       "+photo.getDate());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /***
     * Controller function that lead you back to the page of all of the users' albums, the home page.
     * @param actionEvent  occurs when clicking on the back button to go back to the home page of the album application website
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void backToUserSettingsHandler(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Stage stage = Photos.getStage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserSubSystem.fxml"));
        AnchorPane root = loader.load();
        UserSubSystemController userSubSystemController = loader.getController();
        userSubSystemController.start(stage, this.currUser);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     * Controller function that lead you back to the login page of the album application.
     * @param actionEvent occurs when clicking on the logout button to make sure the user logs in again
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void LogOutHandler(ActionEvent actionEvent) throws IOException {
        Stage stage = Photos.getStage();
        FXMLLoader loader = new FXMLLoader();
        AnchorPane root = loader.load(getClass().getResource("UserLogin.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     * Controller function that safely exits the application.
     * @param actionEvent occurs when clicking on the quit button
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void QuitHandler(ActionEvent actionEvent) {
        System.exit(1);
    }

    /***
     * Controller function that allows the user to upload photos to add it to the current album.
     * @param actionEvent occurs when clicking on the add photo button to allow the user to select a photo from their disk
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void AddPhotoHandler(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(Photos.getStage());
        if (file != null){
            Photo photo = new Photo();
            photo.setImage(file.getPath());

            for (Photo temp : observableList){
                if (temp.getPath().equals(photo.getPath())){
                    UserListController.showAlert("Error", "Photo has already been added", "Please add a photo that has not been added");
                    return;
                }
            }

            this.currAlbum.getPhotoList().add(photo);
            this.observableList.add(photo);
            this.photoListView.setItems(this.observableList);
            UserListController.write(this.users);
        }
    }

    /***
     * Controller function that allows the user to remove the photo selected on the album.
     * @param actionEvent
     * @throws IOException
     */
    public void DeletePhotoHandler(ActionEvent actionEvent) throws IOException {
        if (this.photoListView.getSelectionModel().getSelectedItem() == null) return;
        int index = this.photoListView.getSelectionModel().getSelectedIndex();
        this.observableList.remove(index);
        this.photoListView.setItems(this.observableList);

        this.currAlbum.getPhotoList().remove(index);

        for (User user : this.users){
            if (user.getUsername().equals(this.currUser.getUsername())){
                ArrayList<Album> userAlbums = user.getAlbums();
                for (Album album : userAlbums){
                    if (album.getAlbumName().equals(this.currAlbum.getAlbumName())) album.setPhotoList(this.currAlbum.getPhotoList());
                }
            }
        }
        UserListController.write(this.users);
    }

    /***
     * Controller function that allows the user to add a caption to a selected photo.
     * @param actionEvent
     * @throws IOException
     */
    public void captionHandler(ActionEvent actionEvent) throws IOException {
        if (this.photoListView.getSelectionModel().getSelectedItem() == null) return;

        int index = this.photoListView.getSelectionModel().getSelectedIndex();

        Photo photo = this.observableList.get(index);

        //TextInputDialog textInputDialog = new TextInputDialog();
        TextInputDialog textInputDialog = getTextInputDialog("Caption", "Enter caption", "Caption");
        /*textInputDialog.setTitle("Caption");
        textInputDialog.setHeaderText("Enter caption");
        textInputDialog.getDialogPane().setContentText("Caption");*/
        Optional<String> enteredCaption = textInputDialog.showAndWait();

        if (!enteredCaption.isPresent()) photo.setCaption("");
        else photo.setCaption(enteredCaption.get());

        this.observableList.set(index, photo);
        this.photoListView.setItems(this.observableList);

        this.currAlbum.getPhotoList().get(index).setCaption(enteredCaption.get());
        UserListController.write(this.users);
    }

    /***
     * Creates the text input dialog based on the title, headerText, and contentText of the dialog.
     * @param title of the textInputDialog window/subwindow
     * @param headerText represents the header inside the textInputDialog
     * @param contentText tells the user whatever to type in
     * @return a TextInputDialog that request user input.
     */
    private static TextInputDialog getTextInputDialog(String title, String headerText, String contentText) {
        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle(title);
        textDialog.setHeaderText(headerText);
        textDialog.getDialogPane().setContentText(contentText);
        return textDialog;
    }

    /***
     * Controller function that adds the tag, (key, value) pair, to the photo that you selected on the current photo album.
     * @param actionEvent occurs when clicking on the add tag button and entering the tag for the photo that you have selected
     * @throws IOException if the user hasn't entered the tag correctly
     */

    public void addTagHandler(ActionEvent actionEvent) throws IOException {
        //Case where user selected a photo from an empty album.
        if(photoListView.getSelectionModel().getSelectedItem() == null) return;
        //Get the index of the photo that you are adding the tag to.
        int photoIndex = photoListView.getSelectionModel().getSelectedIndex();
        Photo photoTagged = observableList.get(photoIndex);

        //Create a new Dialog that allows the user to enter the tag
        TextInputDialog enterTag = getTextInputDialog("Photo Tag", "Please enter the tag for the photo.", "Tag");
        Optional<String> result = enterTag.showAndWait();

        //Get the string tag as the key, as long as you entered a string.
        String tagName = (!result.isPresent()) ? null : result.get();
        if(tagName == null) return;

        //Create a new Dialog to add the value for the tag and then retrieve it the same way.
        TextInputDialog valueOfTag = getTextInputDialog("Value of tag", "Enter the value for the tag you applied for the photo.","Value");
        result = valueOfTag.showAndWait();

        //Get the string tag as the key, as long as you entered a string.
        String valueName = (!result.isPresent()) ? null : result.get();
        if(valueName == null) return;

        //Add the tag to the photo
        photoTagged.addTag(tagName, valueName);
        //Find the photo in the observable list and set it to that tagged photo.
        observableList.set(photoIndex, photoTagged);
        //Display the list of photos to the frontend.
        photoListView.setItems(observableList);

        currAlbum.getPhotoList().set(photoIndex, photoTagged);
        UserListController.write(users);
    }

    /***
     * Controller function that deletes the tag (key, value) pair entered through a TextInputDialog of the photo that you have selected.
     * @param actionEvent if you click on the delete tag button for the photo you have selected and enter the tag you want to delete
     * @throws IOException if the user hasn't entered the tag correctly
     */
    public void deleteTagHandler(ActionEvent actionEvent) throws IOException {
        //Case where user selected a photo from an empty album.
        if(photoListView.getSelectionModel().getSelectedItem() == null) return;
        //Get the index of the photo that you are adding the tag to.
        int photoIndex = photoListView.getSelectionModel().getSelectedIndex();
        Photo photoTagged = observableList.get(photoIndex);

        //Create a new Dialog that allows the user to enter the tag they want to delete
        TextInputDialog enterTag = getTextInputDialog("Del Tag", "Please enter the tag you want to delete.", "Tag");
        Optional<String> result = enterTag.showAndWait();

        //Get the string tag as the key, as long as you entered a string.
        String tagName = (!result.isPresent()) ? null : result.get();
        if(tagName == null) return;

        //Create a new Dialog to add the value for the tag and then retrieve it the same way.
        TextInputDialog valueOfTag = getTextInputDialog("Value of tag to delete", "Enter the value of the tag to delete.", "Value");
        Optional<String> anotherResult = valueOfTag.showAndWait();

        //Get the string tag as the key, as long as you entered a string.
        String valueName = (!anotherResult.isPresent()) ? null : result.get();
        if(valueName == null) return;
        //Add the tag to the photo
        photoTagged.removeTag(tagName, valueName);
        //Find the photo in the observable list and set it to that tagged photo.
        observableList.set(photoIndex, photoTagged);
        //Display the list of photos to the frontend.
        photoListView.setItems(observableList);

        currAlbum.getPhotoList().set(photoIndex, photoTagged);
        UserListController.write(users);
    }

    /***
     * Controller function that displays the images in the current album as a slideshow.
     * @param actionEvent
     * @throws IOException
     */
    public void slideshowHandler(ActionEvent actionEvent) throws IOException {
        if (this.currAlbum.getPhotoList().size() == 0) {
            UserListController.showAlert("Error", "No photos in album", "Please add photos in album first");
            return;
        }

        Stage stage = Photos.getStage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SlideShow.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        SlideShowController slideShowController = loader.getController();

        slideShowController.start(stage, this.currUser, this.currAlbum, 0);

        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     * Controller function that displays the photo the user has selected along with it's tags.
     * @param actionEvent occurs when the user clicks on the photo and creates a new scene to display the photo along with its tags
     * @throws IOException
     */
    public void displayPhotoHandler(ActionEvent actionEvent) throws IOException {
        Stage stage = Photos.getStage();
        //get the selected photo on the album
        Photo selectedPhoto = this.photoListView.getSelectionModel().getSelectedItem();

        if (selectedPhoto == null) {
            UserListController.showAlert("Error", "No photo selected", "Please select a picture to display");
            return;
        }

        int index = this.currAlbum.getPhotoList().indexOf(selectedPhoto);
        if (index == -1) {
            UserListController.showAlert("Photo Not Found", "Error", "Selected photo not found in the album");
            return;
        }

        Photo selectedAlbumPhoto = this.currAlbum.getPhotoList().get(index);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayScreen.fxml"));
        AnchorPane root = loader.load();
        DisplayScreenController displayScreenController = loader.getController();
        displayScreenController.start(stage, this.currUser, this.currAlbum, selectedAlbumPhoto);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     * Controller function that allows the user to move the photo from one album to another.
     * @param actionEvent occurs when clicking on the move to album button and allowing the
     * user to enter the albumName to move to in a TextInputDialog window
     * @throws IOException
     */
    public void movePhotoHandler(ActionEvent actionEvent) throws IOException {
        if (this.photoListView.getSelectionModel().getSelectedItem() == null) {
            UserListController.showAlert("Error", "No Photo picked", "Please pick a photo that exists");
            return;
        }
        Photo selectedPhoto = this.currAlbum.getPhotoList().get(this.photoListView.getSelectionModel().getSelectedIndex());

        TextInputDialog textInputDialog = getTextInputDialog("Move Photo", "Enter name", "Name of receiving album");
        Optional<String> result = textInputDialog.showAndWait();

        if (!result.isPresent() || result.get().isEmpty()) {
            UserListController.showAlert("Error", "Invalid Album", "Please add a valid Album name");
            return;
        }
        String receivingAlbumName = result.get();
        if (receivingAlbumName.equals(this.currAlbum.getAlbumName())) {
            UserListController.showAlert("Error", "Invalid Album", "Please choose a different Album");
            return;
        }

        if (!doesAlbumExist(result.get())) {
            UserListController.showAlert("Album Doesn't Exist", "Enter Valid Album Name", "Please add a valid Album name");
            return;
        }

        for (User user : this.users) {
            if (user.getUsername().equals(this.currUser.getUsername())) {
                for (Album album : user.getAlbums()) {
                    if (album.getAlbumName().equals(receivingAlbumName)) {
                        if (album.getPhotoList().contains(selectedPhoto)) {
                            UserListController.showAlert("Duplicate Photo", "Add Different Photos", "No duplicates allowed");
                            return;
                        }
                        album.addPhoto(selectedPhoto);
                    }
                    if (album.getAlbumName().equals(this.currAlbum.getAlbumName())) {
                        album.getPhotoList().remove(this.photoListView.getSelectionModel().getSelectedIndex());
                    }
                }
            }
        }

        this.observableList.remove(this.photoListView.getSelectionModel().getSelectedIndex());
        UserListController.write(this.users);
    }

    /***
     * Helper method that checks to see if the albumName exists before moving or copying a photo to that album.
     * @param receivingAlbumName is the name of the album
     * @return true if there is an album named as receivingAlbumName and false if not
     */
    private boolean doesAlbumExist(String receivingAlbumName) {
        for (Album album : this.currUser.getAlbums()){
            if (album.getAlbumName().equals(receivingAlbumName)) return true;
        }
        return false;
    }

    /***
     * Controller function that allows the user to copy the photo to the albumName
     * entered by the user in the TextInputDialog.
     * @param actionEvent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void copyPhotoHandler(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (this.photoListView.getSelectionModel().getSelectedItem() == null) {
            UserListController.showAlert("Error", "No Photo picked", "Please pick a photo that exists");
            return;
        }

        Photo selectedPhoto = this.currAlbum.getPhotoList().get(this.photoListView.getSelectionModel().getSelectedIndex());

        TextInputDialog textInputDialog = getTextInputDialog("Copy Photo", "Enter name","Name of receiving album" );
        Optional<String> result = textInputDialog.showAndWait();

        if (!result.isPresent() || result.get().isEmpty()) {
            UserListController.showAlert("Error", "Invalid Album", "Please add a valid Album name");
            return;
        }
        String receivingAlbumName = result.get();
        if (receivingAlbumName.equals(this.currAlbum.getAlbumName())) {
            UserListController.showAlert("Error", "Invalid Album", "Please choose a different Album");
            return;
        }

        if (!doesAlbumExist(result.get())) {
            UserListController.showAlert("Album Doesn't Exist", "Enter Valid Album Name", "Please add a valid Album name");
            return;
        }

        for (User user : this.users) {
            if (user.getUsername().equals(this.currUser.getUsername())) {
                for (Album album : user.getAlbums()) {
                    if (album.getAlbumName().equals(receivingAlbumName)) {
                        if (album.getPhotoList().contains(selectedPhoto)) {
                            UserListController.showAlert("Duplicate Photo", "Add Different Photos", "No duplicates allowed");
                            return;
                        }
                        album.addPhoto(selectedPhoto);
                    }
                }
            }
        }

        UserListController.write(this.users);

    }

    /***
     * Controller function that filters out the photos via a date range selected by the user.
     * @param actionEvent occurs when clicking on Search By Date button under the Filter Drop Down Menu
     */
    public void filterByDateHandler(ActionEvent actionEvent) {
        DatePicker datePicker = new DatePicker();
        Dialog dialog = getDialog("Start Date", "Enter", datePicker);
        Optional<Date> result =  dialog.showAndWait();

        Date startDate = java.util.Date.from(datePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        DatePicker datePicker1 = new DatePicker();
        Dialog dialog1 = getDialog("End Date", "Enter", datePicker1);
        result = dialog1.showAndWait();

        Date endDate = java.util.Date.from(datePicker1.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        ObservableList<Photo> photoObservableList = FXCollections.observableArrayList();
        for (Photo photo : this.observableList){
            Date date = photo.getDate();
            if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) photoObservableList.add(photo);
        }
        this.observableList = photoObservableList;
        this.photoListView.setItems(this.observableList);
        this.photoListView.refresh();
    }

    /***
     * Returns a Dialog instance that allows the user to select dates.
     * @param title of the dialog instance, all the way at the top
     * @param headerText as the header for the dialog
     * @param datePicker allows the user to pick at date from a calendar in the DatePicker instance
     * @return the new Dialog instance.
     */
    private static Dialog<String> getDialog(String title, String headerText, DatePicker datePicker) {
        Dialog dialog = new Dialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.getDialogPane().setContent(datePicker);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        return dialog;
    }

    /***
     * Controller function that filters out photos in the current photo album by tag (key, value) pair
     * entered through a TextInputDialog.
     * @param actionEvent, clicking on the filter by tag val1 button in the filter drop down menu to
     * filter out photos that contain the (tag, val1) pair
     */
    public void tagHandler(ActionEvent actionEvent) {
        //Create a TextInputDialog to allow the user to enter the tag type
        TextInputDialog tagName = getTextInputDialog("Tag Name", "Please enter the name of the tag you want to filter photos by.", "Tag");
        Optional<String> result = tagName.showAndWait();
        //Retrieving tag type.
        String tagType = (!result.isPresent()) ? null : result.get();
        if(tagType == null) return;
        //Do the same in the previous step for the user to enter the tag value
        TextInputDialog tagValue = getTextInputDialog("Tag Value", "Please enter the value of the tag you want to filter photos by.", "Value");
        Optional<String> resultVal = tagValue.showAndWait();
        //Retrieving tag value.
        String tagVal = (!resultVal.isPresent()) ? null : resultVal.get();
        if(tagVal == null) return;
        //Once you have retrieved the tag type and value, then put all the photos from the observable Arraylist into
        //a filtered arraylist that match the tag.
        ObservableList<Photo> filteredList = FXCollections.observableArrayList();
        for (Photo photo: this.observableList) {
            //For each tag in the photo
            for (Tag tag: photo.getTagList()) {
                //If the tag in the photo matches the tag that you are filtering out by
                if(tag.getTagName().equals(tagType) && tag.getTagValue().equals(tagVal)) {
                    //Then just add the photo in the filteredList.
                    filteredList.add(photo);
                    //Break.
                    break;
                }
            }
        }
        //Set the album in the frontend to contain the photos in the filteredList.
        this.observableList = filteredList;
        photoListView.setItems(this.observableList);
        photoListView.refresh();
    }

    /***
     * Controller function that filters out photos in the current photo album containing tag1 or tag2
     * @param actionEvent occurs when clicking on the Tag1 or Tag2 button and allowing the user to
     * enter each tag in each TextInputDialog
     */

    public void tagOrHandler(ActionEvent actionEvent) {
        //Create two text input dialogs, one for each tag and retrieve both tags from user input.
        TextInputDialog tagDialog1 = getTextInputDialog("Tag 1", "Please enter tag1 in this format: tagName,value.", "tagName,value");
        Optional<String> resultTag1 = tagDialog1.showAndWait();
        String tag1 = (!resultTag1.isPresent()) ? null : resultTag1.get();
        if(tag1 == null) return;
        //Check to see if the result has been parsed in correctly.
        //Split the tag based on the comma.
        String[] tag1Tuple = tag1.split(",");
        //While the number of tokens is less than 2 in that tag
        if(tag1Tuple.length != 2) {
            //Send an alert to the user telling it to enter the tag properly again.
            UserListController.showAlert("Error: Invalid tag1", "You have entered tag1 in an incorrect format.", "Please enter tag1 again in this format : tagName,value.");
            return;
        }
        //First token is name and second token is value.
        String tag1Name = tag1Tuple[0];
        String tag1Value = tag1Tuple[1];
        //Repeat these steps for tag 2.
        TextInputDialog tagDialog2 = getTextInputDialog("Tag 2", "Please enter tag2 in this format : tagName,value.", "tagName,value");
        Optional<String> resultTag2 = tagDialog2.showAndWait();
        String tag2 = (!resultTag2.isPresent()) ? null : resultTag2.get();
        if(tag2 == null) return;
        //Check to see if the result has been parsed in correctly.
        //Split the tag based on the comma.
        String[] tag2Tuple = tag2.split(",");
        //While the number of tokens is less than 2 in that tag
        if(tag2Tuple.length != 2) {
            //Send an alert to the user telling it to enter the tag properly again.
            UserListController.showAlert("Error: Invalid tag2", "You have entered tag2 in an incorrect format.", "Please enter tag2 again in this format : tagName,value.");
            return;
        }
        //First token is name and second token is value.
        String tag2Name = tag2Tuple[0];
        String tag2Value = tag2Tuple[1];
        //Have a filtered list of photos where each of them contains either one of those tags
        ObservableList<Photo> filteredPhotoList = FXCollections.observableArrayList();
        //For each photo in the observable list
        for(Photo photo: this.observableList) {
            //For each tag in the current photo
            for(Tag tag: photo.getTagList()) {
                //If the tag matches the first or second tag
                if((tag.getTagName().equals(tag1Name) && tag.getTagValue().equals(tag1Value)) ||
                        (tag.getTagName().equals(tag2Name) && tag.getTagValue().equals(tag2Value))) {
                    //Then add the photo to the filtered list of photos
                    filteredPhotoList.add(photo);
                    //Break
                    break;
                }
            }
        }
        //Set the album in the frontend to contain the photos in the filteredList.
        this.observableList = filteredPhotoList;
        photoListView.setItems(this.observableList);
        photoListView.refresh();
    }

    /***
     * Controller function that filters out photos in the current photo album containing tag1 and tag2
     * @param actionEvent occurs when clicking on the Tag1 or Tag2 button and allowing the user to
     * enter each tag in each TextInputDialog
     */
    public void tagAndHandler(ActionEvent actionEvent) {
        //Create two text input dialogs, one for each tag and retrieve both tags from user input.
        TextInputDialog tagDialog1 = getTextInputDialog("Tag 1", "Please enter tag1 in this format: tagName,value.", "tagName,value");
        Optional<String> resultTag1 = tagDialog1.showAndWait();
        String tag1 = (!resultTag1.isPresent()) ? null : resultTag1.get();
        if(tag1 == null) return;
        //Check to see if the result has been parsed in correctly.
        //Split the tag based on the comma.
        String[] tag1Tuple = tag1.split(",");
        //While the number of tokens is less than 2 in that tag
        if(tag1Tuple.length != 2) {
            //Send an alert to the user telling it to enter the tag properly again.
            UserListController.showAlert("Error: Invalid tag1", "You have entered tag1 in an incorrect format.", "Please enter tag1 again in this format : tagName,value.");
            return;
        }
        //First token is name and second token is value.
        String tag1Name = tag1Tuple[0];
        String tag1Value = tag1Tuple[1];
        //Repeat these steps for tag 2.
        TextInputDialog tagDialog2 = getTextInputDialog("Tag 2", "Please enter tag2 in this format : tagName,value.", "tagName,value");
        Optional<String> resultTag2 = tagDialog2.showAndWait();
        String tag2 = (!resultTag2.isPresent()) ? null : resultTag2.get();
        if(tag2 == null) return;
        //Check to see if the result has been parsed in correctly.
        //Split the tag based on the comma.
        String[] tag2Tuple = tag2.split(",");
        //While the number of tokens is less than 2 in that tag
        if(tag2Tuple.length != 2) {
            //Send an alert to the user telling it to enter the tag properly again.
            UserListController.showAlert("Error: Invalid tag2", "You have entered tag2 in an incorrect format.", "Please enter tag2 again in this format : tagName,value.");
            return;
        }
        //First token is name and second token is value.
        String tag2Name = tag2Tuple[0];
        String tag2Value = tag2Tuple[1];
        //Have a filtered list of photos where each of them contains either one of those tags
        ObservableList<Photo> filteredPhotoList = FXCollections.observableArrayList();
        //For each photo in the observable list
        for(Photo photo: this.observableList) {
            //Add the photo to the filteredPhotoslist if it contains both tag1 and tag2
            boolean tag1Exists = false;
            boolean tag2Exists = false;
            for(Tag tag: photo.getTagList()){
                if(tag.getTagName().equals(tag1Name) && tag.getTagValue().equals(tag1Value)) tag1Exists = true;
                if(tag.getTagName().equals(tag2Name) && tag.getTagValue().equals(tag2Value)) tag2Exists = true;
                if(tag1Exists && tag2Exists){
                    //Add the photo to the filtered list of photos
                    filteredPhotoList.add(photo);
                    //Break
                    break;
                }
            }
        }
        //Set the album in the frontend to contain the photos in the filteredList.
        this.observableList = filteredPhotoList;
        photoListView.setItems(this.observableList);
        photoListView.refresh();
    }

    /***
     * Method that checks to see if the string argument contains alphanumeric chars (letters, or letters and/or digits).
     * @param string is the word that you pass in
     * @return true if string is alphanumeric and false if it's not
     */
    public boolean isAlphaNum(String string){
        for (char character: string.toCharArray()) {
            if(Character.isLetter(character) || Character.isDigit(character)) return true;
        }
        return false;
    }

    /***
     * Controller function that allows the user to create a new album to store all the filter photos in.
     * @param actionEvent occurs when clicking on the Create Album (Filtered) button
     * and creates a new TextInputDialog window to prompt the user to enter the albumName to store the photos in.
     * @throws IOException
     */
    public void createFilteredAlbumHandler(ActionEvent actionEvent) throws IOException {
        //Create a TextInputDialog that allows the user to enter the album name that stores the filtered photos.
        TextInputDialog dialog = getTextInputDialog("Filtered Album Name", "Please enter the name of the album where you would like to store the displayed photos", "Name");
        Optional<String> result = dialog.showAndWait();
        //Retrieve the album name and check to see if the album name is valid.
        String albumName = (!result.isPresent()) ? null : result.get();
        if(albumName == null) return;
        else if(albumName.equals("")){
            UserListController.showAlert("Empty Album Name", "Album name is empty", "Please enter an album name.");
            return;
        } else if(doesAlbumExist(albumName)){
            UserListController.showAlert("Existing Album", "Album name already exists", "Please enter a different album name.");
            return;
        } else if(!isAlphaNum(albumName)){ //If the album name also only contains non alphanumeric chars.
            UserListController.showAlert("Invalid Album Name", "Non-alphanumeric char based albumName.", "Please enter an album name containing alphanumeric characters ((A-Z) (Uppercase and/or Lowercase) and/or 0-9.");
            return;
        }
        //Create a new album to store the filtered photos.
        Album filteredAlbum = new Album(albumName);
        //Store each of the filtered photos there.
        for (Photo photo: this.observableList) {
            filteredAlbum.getPhotoList().add(photo);
        }
        //Add the album to the users list of albums for user retrieval.
        currUser.addAlbum(filteredAlbum);
        //Write the list of users and it's albums to the frontend.
        for (User user: users) {
            if(user.getUsername().equals(currUser.getUsername())){
                user.addAlbum(filteredAlbum);
            }
        }
        UserListController.write(users);
    }

    /***
     * Controller function that clears out all the filters in the entire album
     * @param actionEvent
     */

    public void clearFilterHandler(ActionEvent actionEvent) {
        this.tag1 = null;
        this.observableList = FXCollections.observableArrayList(this.currAlbum.getPhotoList());
        this.photoListView.refresh();
        this.photoListView.setItems(this.observableList);
    }
}
