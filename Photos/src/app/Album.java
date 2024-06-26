package app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Album class to hold photos in an arraylist
 */
public class Album implements Serializable{

    private ArrayList<Photo> photoList = new ArrayList<>();
    private String albumName;
    private static final long serialVersionUID = 1L;

    public Album(String albumName){
        this.albumName = albumName;
    }
    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String albumName){
        this.albumName = albumName;
    }

    public ArrayList<Photo> getPhotoList() {
        return this.photoList;
    }

    public void setPhotoList(ArrayList<Photo> photoList) {
        this.photoList = photoList;
    }

    public void addPhoto(Photo photo){
        photoList.add(photo);
    }
}
