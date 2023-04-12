package photos.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import photos.app.UserDataController;

public class User implements Serializable
{
	private static final long serialVersionUID = 2L;
	private String userName;
	private ArrayList<Album> album = new ArrayList<>();
	public static Album currentSessionAlbum;
	
	public User()
	{
		userName = null;
	}
	public User(String nm)
	{
		userName = nm;
	}
	
	public void setUserName(String nm)
	{
		userName = nm;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public void addAlbum(Album a)
	{
		album.add(a);
	}
	
	public void removeAlbum(String selectedAlbum) 
	{
	    Iterator<Album> iterator = album.iterator();
	    while(iterator.hasNext())
	    {
	        Album a = iterator.next();
	        if(a.getAlbumName().equals(selectedAlbum))
	        {
	            iterator.remove();
	        }
	    }
	}
	
	public void renameAlbum(String previousName, String newName)
	{
		for(Album a: album)
		{
			if(a.getAlbumName().equals(previousName))
			{
				a.setAlbumName(newName);
			}
		}
	}
	
	public ArrayList<Album> getAlbumList()
	{
		return album;
	}
	
	public void setCurrentSessionAlbum(String albumNm)
	{
		User currentUser = UserDataController.getCurrentSessionUser();
		ArrayList<Album> allAlbum = currentUser.getAlbumList();
		for(Album a: allAlbum)
		{
			if(a.getAlbumName().equals(albumNm))
			{
				currentSessionAlbum = a;
			}
		}
	}
	
	public static Album getCurrentSessionAlbum()
	{
		return currentSessionAlbum;
	}
}