package photos.control;

import java.io.Serializable;

public class Photo implements Serializable
{
	private String photoName;
	private String imagePath;
	
	public Photo()
	{
		photoName = null;
		imagePath = null;
	}
	public Photo(String nm, String ip)
	{
		photoName = nm;
		imagePath = ip;
	}
	
	public void setPhotoName(String nm)
	{
		photoName = nm;
	}
	public void setImagePath(String ip)
	{
		imagePath = ip;
	}
}