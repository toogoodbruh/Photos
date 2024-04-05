package view;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import app.Photos;
import app.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * This is a controller class for the Admin sub-system to add or delete users
 */
public class AdminController {

    @FXML
    private Button addNewUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button quitButton;

    @FXML
    private TextField UserTextBox;

    @FXML
    ListView<User> userListView = new ListView<>();

    ArrayList<User> users;
    ObservableList<User> observableList;

    /**
     * This method will add a new user
     * @param actionEvent
     * @throws IOException
     */
    public void addNewUserHandler(ActionEvent actionEvent) throws IOException {
        String username = UserTextBox.getText().trim();

        if(username.isEmpty() || users.contains(username) || username.equalsIgnoreCase("Admin")) return;

        User user = new User(username);
        observableList.add(user);
        users.add(user);

        UserTextBox.setText("");

        UserListController.write(users);
    }

    /**
     * This method will delete a user
     * @param actionEvent
     * @throws IOException
     */
    public void deleteUserHandler(ActionEvent actionEvent) throws IOException {
        int index = userListView.getSelectionModel().getSelectedIndex();
        observableList.remove(index);
        userListView.setItems(observableList);
        users.remove(index);
        UserListController.write(users);
    }

    /**
     * This method will log out of the admin sub-system and go back to the login screen
     * @param actionEvent
     * @throws IOException
     */
    public void onLogOutPress(ActionEvent actionEvent) throws IOException {
        Stage stage = Photos.getStage(); //Gets the current state/stage of the javafX application launched through photos13/src/app/Photos
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserLogin.fxml")); //Creates an FXML loader of the userlogin page.
        AnchorPane root = (AnchorPane) loader.load(); //Loads that into an anchorPane/root into the Stage that represents whatever is in the Stage
        stage.setScene(new Scene(root)); //Scene object is the scene graph, which takes in a root node, AnchorPane, the container of the JavaFX Stage/window.
        stage.show(); //Now display that JavaFX stage for the JavaFX application.
    }

    /**
     * This method will exit the program
     * @param actionEvent
     */
    public void adminQuitHandler(ActionEvent actionEvent) {
        System.exit(1);
    }

    /**
     * This method will display the admin interface and generate a list of all the users
     * @param stage
     * @throws IOException
     */
    public void start(Stage stage) throws IOException{
        try{
            users = UserListController.read();
        }
        catch(FileNotFoundException e){
            users = new ArrayList<>();
            UserListController.write(users);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        observableList = FXCollections.observableArrayList(users);
        userListView.setItems(observableList);

        userListView.setCellFactory(param -> new ListCell<User>(){
            protected void updateItem(User item, boolean isEmpty){
                super.updateItem(item, isEmpty);
                if (isEmpty || item == null || item.getUsername() == null) setText(null);
                else setText(item.getUsername());

            }
        });
    }
}
