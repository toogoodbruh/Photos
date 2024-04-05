package view;

import app.User;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This is a controller/class to manage user data and handle errors.
 */
public class UserListController implements Serializable {

    private static final String DEFAULT_LOCATION = "Users.dat";
    private static String location = DEFAULT_LOCATION;

    /**
     * Sets the location of the user data file.
     *
     * @param fileLocation The location of the user data file.
     */
    public static void setLocation(String fileLocation) {
        location = fileLocation;
    }

    /**
     * Writes the list of users to a file.
     *
     * @param userArrayList The list of users to be written.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void write(ArrayList<User> userArrayList) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(location))) {
            objectOutputStream.writeObject(userArrayList);
        }
    }

    /**
     * Reads the list of users from a file.
     *
     * @return The list of users read from the file.
     * @throws IOException            If an I/O error occurs while reading from the file.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public static ArrayList<User> read() throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(location))) {
            return (ArrayList<User>) objectInputStream.readObject();
        }
    }

    /**
     * Displays an alert with the specified title, header, and content.
     *
     * @param title   The title of the alert.
     * @param header  The header text of the alert.
     * @param context The content text of the alert.
     */
    public static void showAlert(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
}
