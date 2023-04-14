package photos.control;
import java.io.Serializable;
import java.util.ArrayList;

import photos.app.UserDataController;

public class Album implements Serializable
{
	private static final long serialVersionUID = 4L;
	private ArrayList<Photo> photos = new ArrayList<>();
	private String albumName;
	
	public Album()
	{
		albumName = null;
	}
	
	public Album(String nm)
	{
		albumName = nm;
	}
	
	public Album(String nm, ArrayList<Photo> p)
	{
		albumName = nm;
		photos = p;
	}
	
	public void setAlbumName(String nm)
	{
		albumName = nm;
	}
	
	public String getAlbumName()
	{
		return albumName;
	}
	
	public void addPhotos(Photo p)
	{
		photos.add(p);
	}
	
	public int PhotosNum()
	{
		return this.getPhotoList().size();
	}
	
	public ArrayList<Photo> getPhotoList()
	{
		return photos;
	}
	
	public String dateRange()
	{
		String minDR = "99/99/9999";
		String maxDR = "00/00/0000";
		ArrayList<Photo> p = this.getPhotoList();
		if(p.size() == 0)
		{
			return "N/A";
		}
		for(Photo dP: p)
		{
			if(minDR.substring(6).compareTo(dP.getLastModDate().substring(6)) >= 0)
			{
				minDR = dP.getLastModDate();
				if(minDR.substring(3, 5).compareTo(dP.getLastModDate().substring(3,5)) >= 0)
				{
					minDR = dP.getLastModDate();
					if(minDR.substring(0, 2).compareTo(dP.getLastModDate().substring(0, 2)) >= 0)
					{
						minDR = dP.getLastModDate();
					}
				}
			}
			if(maxDR.substring(6).compareTo(dP.getLastModDate().substring(6)) <= 0)
			{
				maxDR = dP.getLastModDate();
				if(maxDR.substring(3, 5).compareTo(dP.getLastModDate().substring(3,5)) <= 0)
				{
					maxDR = dP.getLastModDate();
					if(maxDR.substring(0, 2).compareTo(dP.getLastModDate().substring(0, 2)) <= 0)
					{
						maxDR = dP.getLastModDate();
					}
				}
			}
		}
		return minDR.substring(0, 10) + " - " + maxDR.substring(0, 10);
	}

	public Album getAlbum(String selectedItem)
	{
		User u = UserDataController.getCurrentSessionUser();
		ArrayList<Album> a = u.getAlbumList();
		for(Album aR: a)
		{
			if(aR.getAlbumName().equals(selectedItem))
			{
				return aR;
			}
		}
		return null;
	}
}