package app;

import javafx.scene.image.Image;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.MILLISECOND;

/**
 * Photo class to represent the different properties an image has, such as date, tags, captions, and path
 */
public class Photo implements Serializable {

    private transient Image image;
    private String caption = "";
    private String path;
    private Date date;
    private ArrayList<Tag> tagList;

    /**
     *
     * Photo
     * @throws   IOException
     * @throws  ClassNotFoundException
     */

    //constructor for Photo Class
    public Photo() throws IOException, ClassNotFoundException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(MILLISECOND, 0);
        this.date = calendar.getTime();
    }

    /**
     * get date of an image
     * @return
     */
    public Date getDate(){
        return this.date;
    }

    /**
     * get filepath of an image
     * @return
     */
    public String getPath(){
        return this.path;
    }

    /**
     * get caption of an image
     * @return
     */
    public String getCaption(){
        return this.caption;
    }

    /**
     * set caption of an image
     * @param caption
     */
    public void setCaption(String caption){
        this.caption = caption;
    }

    /**
     * returns an image
     * @return
     * @throws FileNotFoundException
     */
    public  Image getImage() throws FileNotFoundException{
        if (this.image == null && this.path!=null) setImage(this.path);
        return this.image;
    }

    /**
     * sets an image located at a certain filepath
     * @param path
     * @throws FileNotFoundException
     */
    public void setImage(String path) throws FileNotFoundException {
        this.path = path;
        Path filePath = Paths.get(path);
        File f = new File(String.valueOf(filePath));
        Date d = new Date(f.lastModified());
        FileInputStream fis = new FileInputStream(f);
        image = new Image(fis);
        this.date = d;
    }

    /**
     * returns list of tags for image
     * @return
     */
    public ArrayList<Tag> getTagList() {
        if (this.tagList == null) this.tagList = new ArrayList<>();
        return this.tagList;
    }

    /**
     * adds a tag to an image
     * @param tagName
     * @param tagValue
     */
    public void addTag(String tagName, String tagValue){
        if (this.tagList == null) this.tagList = new ArrayList<>();

        for (Tag tag : tagList){
            if (tag.getTagName().equalsIgnoreCase(tagName) && tag.getTagValue().equalsIgnoreCase(tagValue)) return;
        }
        tagList.add(new Tag(tagName, tagValue));
    }

    /**
     * removes a tag from an image
     * @param tagName
     * @param tagValue
     */
    public void removeTag(String tagName, String tagValue){
        if (this.tagList == null) this.tagList = new ArrayList<>();
        for (Tag tag : tagList){
            if (tag.getTagName().equalsIgnoreCase(tagName) && tag.getTagValue().equalsIgnoreCase(tagValue)) {
                tagList.remove(tag);
                return;
            }
        }
    }
}
