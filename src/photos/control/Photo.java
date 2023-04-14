package photos.control;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import photos.control.TagCategory.Tag;

public class Photo implements Serializable
{
	private static final long serialVersionUID = 3L;
	private String caption;
	private String imagePath;
	private String lastModDate;
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	public Photo()
	{
		caption = null;
		imagePath = null;
		setLastModDate(null);
	}
	public Photo(String nm, String ip, String lmd)
	{
		caption = nm;
		imagePath = ip;
		setLastModDate(lmd);
	}
	
	public ArrayList<String> getTags()
	{
		ArrayList<String> tagValues = new ArrayList<String>();
		for(Tag t : tags)
		{
			tagValues.add(t.toString());
		}
		return tagValues;
	}
	
	public boolean containsTag(String tagString)
	{
		for(Tag t : tags)
		{
			if(t.toString().equals(tagString))
				return true;
		}
		return false;
	}
	
	public void addTag(Tag add)
	{
		tags.add(add);
	}
	
	public void setCaption(String nm)
	{
		caption = nm;
		setLastModDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
	}
	
	public String getCaption()
	{
		return caption;
	}
	
	public void setImagePath(String ip)
	{
		imagePath = ip;
		setLastModDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
	}
	
	public String getImagePath()
	{
		return imagePath;
	}
	
	public String getLastModDate()
	{
		return lastModDate;
	}
	
	public void setLastModDate(String lastModDate)
	{
		this.lastModDate = lastModDate;
	}
	
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