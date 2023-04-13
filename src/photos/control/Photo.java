package photos.control;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Photo implements Serializable
{
	private static final long serialVersionUID = 3L;
	private String caption;
	private String imagePath;
	private String lastModDate;
	private Tag tag;
	private ArrayList<String> typeCategory;
	
	public Photo()
	{
		caption = null;
		imagePath = null;
		setLastModDate(null);
		setTag(null);
		typeCategory = new ArrayList<>();
		typeCategory.add("person");
		typeCategory.add("location");
		typeCategory.add("place");
	}
	public Photo(String nm, String ip, String lmd, Tag tag)
	{
		caption = nm;
		imagePath = ip;
		setLastModDate(lmd);
		setTag(tag);
		typeCategory = new ArrayList<>();
		typeCategory.add("person");
		typeCategory.add("location");
		typeCategory.add("place");
	}
	
	public ArrayList<String> getTypeCategories()
	{
		return typeCategory;
	}
	
	public void addTypeCategory(String add)
	{
		typeCategory.add(add);
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
	
	public Tag getTag()
	{
		return tag;
	}
	
	public void setTag(Tag tag)
	{
		this.tag = tag;
	}
}