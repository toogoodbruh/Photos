package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import app.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *  This is a login controller class to allow the user to log in to the application
 */
public class UserLoginController {

    @FXML
    private TextField userInputField;

    @FXML
    private Button LoginButton;

    @FXML
    private Button LoginQuitButton;

    /**
     * This is a method to allow a user to login after typing in a username and clicking the login button
     * @param actionEvent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @FXML
    public void onLoginPress(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        String username = userInputField.getText();
        Path path = Paths.get("../Photos","src","userinfo","Users.dat");
        File f = new File(String.valueOf(path));

        if (username.equalsIgnoreCase("Admin")){
            Stage stage = Photos.getStage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            AdminController adminController = loader.getController();
            adminController.start(stage);
            stage.setScene(new Scene(root));
            stage.show();
        }
        else if (!f.exists()) UserListController.showAlert("Error", "No users present", "Admin has to add new users");
        else if (findUser(username) != null){
            User currUser = findUser(username);
            Stage stage = Photos.getStage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserSubSystem.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            UserSubSystemController controller = loader.getController();
            controller.start(stage,currUser);
            stage.setScene(new Scene(root));
            stage.show();
        }
        else UserListController.showAlert("Error", "User does not exist", "Invalid username");

    }

    /**
     * This is a method to terminate the program
     * @param actionEvent
     */
    public void OnActionQuit(ActionEvent actionEvent) {
        System.exit(1);
    }

    /**
     * This is a method to check if a user exists and to return the user if it does
     * @param username
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public User findUser(String username) throws IOException, ClassNotFoundException{
        ArrayList<User> users = UserListController.read();
        for (User user : users){
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }

    /**
     * This is a method to load all the stock photos that are supposed to come pre-loaded with the application
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadStockPhotos() throws IOException, ClassNotFoundException{
        Path path = Paths.get("photos","src","userinfo","Users.dat");
        File f = new File(String.valueOf(path));
        if (f.exists()) return;

            Photo p1 = new Photo();
            Photo p2 = new Photo();
            Photo p3 = new Photo();
            Photo p4 = new Photo();
            Photo p5 = new Photo();

            Path path1 = Paths.get("../Photos", "data", "stock1.jpg");
            p1.setImage(path1.toString());
            Path path2 = Paths.get("../Photos","data", "stock2.png");
            p2.setImage(path2.toString());
            Path path3 = Paths.get("../Photos","data", "stock3.png");
            p3.setImage(path3.toString());
            Path path4 = Paths.get("../Photos","data", "stock4.png");
            p4.setImage(path4.toString());
            Path path5 = Paths.get("../Photos","data", "stock5.jpg");
            p5.setImage(path5.toString());

            Album stockAlbum = new Album("stock");
            stockAlbum.getPhotoList().add(p1);
            stockAlbum.getPhotoList().add(p2);
            stockAlbum.getPhotoList().add(p3);
            stockAlbum.getPhotoList().add(p4);
            stockAlbum.getPhotoList().add(p5);

            User stock = new User("stock");
            stock.getAlbums().add(stockAlbum);

            ArrayList<User> users;
            try {
                users = UserListController.read();
            } catch (Exception e) {
                users = new ArrayList<>();
                UserListController.write(users);
            }
            if (findUser("stock") == null) {
            	users.add(stock);
                UserListController.write(users);
            }


    }
}
