package app;
import java.io.Serializable;

/**
 * Tag class to store name and value of a tag of a photo
 */
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tagName;
    private String tagValue;

    /**
     * constructor method for Tag class
     * @param tagName
     * @param tagValue
     */
    public Tag(String tagName, String tagValue){
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    /**
     * returns tag name
     * @return
     */
    public String getTagName(){
        return this.tagName;
    }

    /**
     * returns tag value
     * @return
     */
    public String getTagValue() {
        return this.tagValue;
    }
}
