// This controller will be used to store user objects to a file. We will have methods writeFromAFile and readFromAFile
// which will be called before application opens and closes respectively to store and get data.
// implements Singelton design pattern
// all classes to serialize implements Serializable

package photos.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import photos.control.Album;
import photos.control.Photo;
import photos.control.User;

public class UserDataController implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static UserDataController instance = null;
	private ArrayList<User> users;
	
	public static User currentSessionUser;
	
	private UserDataController()
	{
		users = new ArrayList<>();
		currentSessionUser = null;
		
		//done - stock can't be recreated once deleted - idk how
		User stock = new User("stock");
    	//UserDataController userData = UserDataController.getInstance();
    	users.add(stock);
    	Album stockAlbum = new Album("stock");
    	stock.addAlbum(stockAlbum);
    	
    	Photo p1 = new Photo("Temple1", "src/photos/data/stockimage1.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	Photo p2 = new Photo("Temple2", "src/photos/data/stockimage2.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	Photo p3 = new Photo("Temple3", "src/photos/data/stockimage3.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	Photo p4 = new Photo("Temple4", "src/photos/data/stockimage4.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	Photo p5 = new Photo("Temple5", "src/photos/data/stockimage5.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	
    	stockAlbum.addPhotos(p1);
    	stockAlbum.addPhotos(p2);
    	stockAlbum.addPhotos(p3);
    	stockAlbum.addPhotos(p4);
    	stockAlbum.addPhotos(p5);
	}
	
	public static UserDataController getInstance()
	{
		if(instance == null)
		{
			instance = new UserDataController();
		}
		return instance;
	}
	
	public void addUser(User u)
	{
		users.add(u);
	}
	
	public boolean deleteUser(String username)
	{
		for(User user : users)
		{
			if(user.getUserName().equals(username)) {
				users.remove(user);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<String> getUsernames()
	{
		ArrayList<String> usernames = new ArrayList<String>();
		for(User user: users)
		{
			usernames.add(user.getUserName());
		}
		return usernames;
	}
	public boolean containsUser(String u)
	{
		for(User user: users)
		{
			if(user.getUserName().equals(u))
			{
				return true;
			}
		}
		return false;
	}
	
	public static UserDataController readFromAFile()
	{
		UserDataController loadUsers = UserDataController.getInstance();
		File file = new File("src/photos/data/users.ser");
		try
		{
			if(file.length() == 0)
			{
				//
			}
			else
			{
				try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
				{
					Object o = ois.readObject();
					if(o instanceof ArrayList<?>)
					{
						@SuppressWarnings("unchecked")
						ArrayList<User> userList = (ArrayList<User>) o;
						loadUsers.users = userList;
					}
				}
			}
	    }
		catch(Exception e)
		{
	        e.printStackTrace();
	    }
		
		return loadUsers;
	}
	
	public void writeToAFile()
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/photos/data/users.ser")))
		{
            oos.writeObject(users);
        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
	}

	public void setCurrentSessionUser(String user)
	{
		for(User allUser: users)
		{
			if(allUser.getUserName().equals(user))
			{
				currentSessionUser = allUser;
			}
		}
	}
	
	public static User getCurrentSessionUser()
	{
		return currentSessionUser;
	}
}