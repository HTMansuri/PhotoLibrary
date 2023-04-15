package photos.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a user in the photo album application.
 * 
 * @author Huzaif Mansuri, Pavitra Patel
 */
public class User implements Serializable {

    private static final long serialVersionUID = 2L;
    private String userName;
    private ArrayList<Album> album = new ArrayList<>();
    private ArrayList<TagCategory> categories = new ArrayList<TagCategory>();
    public static Album currentSessionAlbum;

    /**
     * Default constructor for User.
     * Initializes userName to null and adds two default TagCategories.
     */
    public User() {
        userName = null;
        categories.add(new TagCategory("Location", true));
        categories.add(new TagCategory("Person"));
    }

    /**
     * Constructor for User that takes in a username.
     * @param nm the username for the user.
     */
    public User(String nm) {
        userName = nm;
        categories.add(new TagCategory("Location", true));
        categories.add(new TagCategory("Person"));
    }

    /**
     * Setter for the user's username.
     * @param nm the username for the user.
     */
    public void setUserName(String nm) {
        userName = nm;
    }

    /**
     * Getter for the user's username.
     * @return the username for the user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Adds an album to the user's list of albums.
     * @param a the album to add.
     */
    public void addAlbum(Album a) {
        album.add(a);
    }

    /**
     * Removes an album from the user's list of albums.
     * @param selectedAlbum the name of the album to remove.
     */
    public void removeAlbum(String selectedAlbum) {
        Iterator<Album> iterator = album.iterator();
        while(iterator.hasNext()) {
            Album a = iterator.next();
            if(a.getAlbumName().equals(selectedAlbum)) {
                iterator.remove();
            }
        }
    }

    /**
     * Renames an album in the user's list of albums.
     * @param previousName the current name of the album to rename.
     * @param newName the new name for the album.
     */
    public void renameAlbum(String previousName, String newName) {
        for(Album a: album) {
            if(a.getAlbumName().equals(previousName)) {
                a.setAlbumName(newName);
            }
        }
    }

    /**
     * Getter for the user's list of albums.
     * @return the user's list of albums.
     */
    public ArrayList<Album> getAlbumList() {
        return album;
    }

    /**
     * Sets the current session album for the user.
     * @param albumNm the name of the album to set as the current session album.
     */
    public void setCurrentSessionAlbum(String albumNm) {
        User currentUser = UserDataController.getCurrentSessionUser();
        ArrayList<Album> allAlbum = currentUser.getAlbumList();
        for(Album a: allAlbum) {
            if(a.getAlbumName().equals(albumNm)) {
                currentSessionAlbum = a;
            }
        }
    }

    /**
     * Getter for the current session album for the user.
     * @return the current session album for the user.
     */
    public static Album getCurrentSessionAlbum() {
        return currentSessionAlbum;
    }

    /**
     * Adds a TagCategory to the user's list of TagCategories.
     * @param t the TagCategory to add.
     */
    public void addTagCategory(TagCategory t) {
        categories.add(t);
    }
	
    /**
     * Removes the specified tag category from the list of categories.
     *
     * @param selectedCategory the name of the tag category to be removed
     */
    public void removeTagCategory(String selectedCategory) 
    {
        Iterator<TagCategory> iterator = categories.iterator();
        while(iterator.hasNext())
        {
            TagCategory t = iterator.next();
            if(t.getCategoryName().equals(selectedCategory))
            {
                iterator.remove();
            }
        }
    }

    /**
     * Returns a list of all category names.
     *
     * @return an ArrayList of String objects representing category names
     */
    public ArrayList<String> getCategoryList()
    {
        ArrayList<String> categoryList = new ArrayList<String>();
        for(TagCategory c : categories)
        {
            categoryList.add(c.getCategoryName());
        }
        return categoryList;
    }

    /**
     * Returns a list of all tag categories.
     *
     * @return an ArrayList of TagCategory objects representing all categories
     */
    public ArrayList<TagCategory> getCategories()
    {
        return categories;
    }
}