package view;

import app.User;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This is a controller/class to keep data for all the users and make it transferable
 */
public class UserListController implements Serializable{

    static String location = Paths.get("photos13", "src", "userinfo", "Users.dat").toString();

    /**
     * This method updates the list of users and all their attributes
     * @param userArrayList
     * @throws IOException
     */
    public static void write(ArrayList<User> userArrayList) throws IOException{
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Path.of(location)))){
            objectOutputStream.writeObject(userArrayList);
        }
    }

    /**
     * This method reads from the list of users to return and display their attributes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     */
    public static ArrayList<User> read() throws IOException, ClassNotFoundException, FileNotFoundException{
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(location))){
            return (ArrayList<User>) objectInputStream.readObject();
        }
    }

    /**
     * This method is the error handler for the application
     * @param title
     * @param header
     * @param context
     */
    public static void showAlert(String title, String header, String context){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
}
