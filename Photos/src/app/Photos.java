package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import view.UserLoginController;

import javax.swing.*;

/**
 * Main class that will set up the application and make a call to initiate the login screen
 *  @author Nikhil Munagala nsm123
 *  @author Aarav Modi arm360
 */
public class Photos extends Application{

    public static Stage primaryStage;

    public static Stage getStage(){
        return primaryStage;
    }

    public void setStage(Stage stage){
        this.primaryStage = stage;
    }
    public static void main(String[] args){
        launch(args);
    }

    /**
     * loads login screen
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        setStage(stage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/UserLogin.fxml"));
        AnchorPane root=(AnchorPane)loader.load();
        UserLoginController userLoginController = loader.getController();
        userLoginController.loadStockPhotos();
        Scene scene = new Scene(root);
        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.show();
    }
}

