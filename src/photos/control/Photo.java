package photos.control;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Photo implements Serializable
{
	private static final long serialVersionUID = 3L;
	private String caption;
	private String imagePath;
	private String lastModDate;
	
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
	
	public void setCaption(String nm)
	{
		caption = nm;
		setLastModDate(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
	}
	
	public String getCaption()
	{
		return caption;
	}
	
	public void setImagePath(String ip)
	{
		imagePath = ip;
		setLastModDate(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
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
}