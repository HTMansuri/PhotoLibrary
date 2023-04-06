package photos.control;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
	private String userName;
	private ArrayList<Album> album = new ArrayList<>();
	
	public User()
	{
		userName = null;
	}
	public User(String nm)
	{
		userName = nm;
	}
	
	public String setUserName(String nm)
	{
		userName = nm;
		return userName;
	}
	
	public void addAlbum(Album a)
	{
		album.add(a);
	}
}