package photos.control;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import photos.control.TagCategory.Tag;
/**
 * The Photo class represents a photo object, with properties such as caption, image path, last modified date, and a list 
 * of tags. This class provides methods to retrieve and modify these properties, as well as methods to add and remove 
 * tags. The class also implements the Serializable interface to allow for saving and loading Photo objects.
 * 
 * @author Pavitra Patel, Huzaif Mansuri
 */
public class Photo implements Serializable
{
	private static final long serialVersionUID = 3L;
	private String caption;
	private String imagePath;
	private String lastModDate;
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	/**
	 * Constructs a new Photo object with null caption, image path, and last modified date.
	 */
	public Photo()
	{
		caption = null;
		imagePath = null;
		setLastModDate(null);
	}
	
	/** Constructs a new Photo object with the given caption, image path, and last modified date. 
	 * 
	 * @param nm 	the caption of the photo 
	 * @param ip 	the file path to the image file of the photo 
	 * @param lmd 	the last modified date of the photo */
	public Photo(String nm, String ip, String lmd)
	{
		caption = nm;
		imagePath = ip;
		setLastModDate(lmd);
	}
	
	/** Returns a list of strings representing the values of each tag associated with this photo. 
	 * 
	 * @return an ArrayList of strings representing each tag associated with the photo
	 */
	public ArrayList<String> getTags()
	{
		ArrayList<String> tagValues = new ArrayList<String>();
		for(Tag t : tags)
		{
			tagValues.add(t.toString());
		}
		return tagValues;
	}
	
	/** Returns whether this photo contains a tag with the given string representation. 
	 * 
	 * @param tagString the string representation of the tag to search for 
	 * @return true if the photo contains the tag, false otherwise
	 */
	public boolean containsTag(String tagString)
	{
		for(Tag t : tags)
		{
			if(t.toString().equals(tagString))
				return true;
		}
		return false;
	}
	
	/**
	 * Adds the given tag to the list of tags associated with this photo.
	 * @param add the tag to add to the photo
	 */
	public void addTag(Tag add)
	{
		tags.add(add);
	}
	
	/**
	 * Sets the caption of this photo to the given string.
 	 * 
 	 * @param nm the new caption for the photo
	 */
	public void setCaption(String nm)
	{
		caption = nm;
		setLastModDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
	}
	
	/**
	 * Returns the caption of this photo.
	 * @return the caption of the photo
	 */
	public String getCaption()
	{
		return caption;
	}
	
	/**
	 * Sets the image file path of this photo to the given string.
	 * @param ip the new file path for the photo
	 */
	public void setImagePath(String ip)
	{
		imagePath = ip;
		setLastModDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
	}
	
	/**
	 * Returns the file path to the image file of this photo.
	 * @return the file path to the image file of the photo
	 */
	public String getImagePath()
	{
		return imagePath;
	}
	
	/**
	Returns the last modified date of this photo.
	@return the last modified date of the photo
	*/
	public String getLastModDate()
	{
		return lastModDate;
	}
	
	/**
	 * Sets the Last Modified Date for a Photo
	 * @param lastModDate	String form of date
	 */
	public void setLastModDate(String lastModDate)
	{
		this.lastModDate = lastModDate;
	}
	
	/**
	 * Remove a tag
	 * @param selectedTag	Tag in String format
	 */
	public void removeTag(String selectedTag) 
	{
	    Iterator<Tag> iterator = tags.iterator();
	    while(iterator.hasNext())
	    {
	        Tag t = iterator.next();
	        if(t.toString().equals(selectedTag))
	        {
	            iterator.remove();
	        }
	    }
	}
}