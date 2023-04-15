package photos.control;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents an Album containing a collection of Photos.
 */
public class Album implements Serializable
{
	private static final long serialVersionUID = 4L;
	
	/**
	 * An ArrayList containing the photos in this album.
	 */
	private ArrayList<Photo> photos = new ArrayList<>();
	
	/**
	 * The name of the album.
	 */
	private String albumName;
	
	/**
	 * When the default constructor is called the name of the album is set to null.
	 */
	public Album()
	{
		albumName = null;
	}
	
	/**
	 * Constructor with album name argument
	 * 
	 * @param nm	The name of the album
	 */
	public Album(String nm)
	{
		albumName = nm;
	}
	
	/**
	 * Constructor with arguments that specifies the name and photos to be stored in that album
	 * 
	 * @param nm	The name of the album
	 * @param p		The name of the collection that contains Photos of nm album
	 */
	public Album(String nm, ArrayList<Photo> p)
	{
		albumName = nm;
		photos = p;
	}
	
	/**
	 * Sets the name of the album
	 * 
	 * @param nm	The name of album
	 */
	public void setAlbumName(String nm)
	{
		albumName = nm;
	}
	
	/**
	 * Gets the name of the album
	 * 
	 * @return		The name of the album
	 */
	public String getAlbumName()
	{
		return albumName;
	}
	
	/**
	 * Adds a Photo to the collection of Photos in the album
	 * 
	 * @param p		A photo that needs to be added in the album
	 */
	public void addPhotos(Photo p)
	{
		photos.add(p);
	}
	
	/**
	 * Gets the numbers of photos in the album
	 * 
	 * @return		Number of photos
	 */
	public int PhotosNum()
	{
		return this.getPhotoList().size();
	}
	
	/**
	 * Returns the list of photos in this album.
	 * 
	 * @return 		the list of photos in this album
	 */
	public ArrayList<Photo> getPhotoList()
	{
		return photos;
	}
	
	/**
	 * Returns a string representation of the date range of photos in this album.
	 * 
	 * @return 		a string representation of the date range of photos in this album
	 */
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

	/**
	 * Gets the Album object when the name of the album is passed
	 * 
	 * @param selectedItem		The name of the album
	 * @return 					The Album object
	 */
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