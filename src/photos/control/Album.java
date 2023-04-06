package photos.control;
import java.util.ArrayList;

public class Album
{
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
	
	public void setAlbumName(String nm)
	{
		albumName = nm;
	}
	public void addPhotos(Photo p)
	{
		photos.add(p);
	}
}