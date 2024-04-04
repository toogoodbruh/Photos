package app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User class to store properties such as albums and username
 *  @author Nikhil Munagala nsm123
 *  @author Aarav Modi arm360
 */
public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    String username ="";
    private ArrayList<Album> albums = new ArrayList<>();

    /**
     * constructor method for User class
     * @param username
     */
    public User(String username){
        this.username = username;
    }

    /**
     * returns Username of current user
     * @return
     */
    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * adds an album to the user's album list
     * @param album
     */
    public void addAlbum(Album album){
        albums.add(album);
    }

    /**
     * returns all the albums that a user has
     * @return
     */
    public ArrayList<Album> getAlbums() {
        return this.albums;
    }

    /**
     * sets the albums of a user
     * @param albums
     */
    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }
}
