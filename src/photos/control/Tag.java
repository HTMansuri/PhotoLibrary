package photos.control;

import java.io.Serializable;
import java.util.ArrayList;

public class Tag implements Serializable
{
	private static final long serialVersionUID = 4L;
	private ArrayList<String> tags;
	
	public Tag()
	{
		tags = new ArrayList<>();
	}
	
	public ArrayList<String> getTags()
	{
		return tags;
	}
	
	public void addTag(String name, String value)
	{
		tags.add(name + ":" + value);
	}
	
	public void removeTag(String tag)
	{
		tags.remove(tag);
	}
}