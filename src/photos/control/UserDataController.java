// This controller will be used to store user objects to a file. We will have methods writeFromAFile and readFromAFile
// which will be called before application opens and closes respectively to store and get data.
// implements Singelton design pattern
// all classes to serialize implements Serializable

package photos.control;

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

/** 
 * The UserDataController class represents a controller for handling user data in the system.
 * This class is responsible for managing the list of users, adding and deleting users, and reading and writing user data to a file.
 * It also provides methods for setting and retrieving the current session user.
 * This class implements the Serializable interface to allow for serialization of user data to a file.
 * 
 * @author Huzaif Mansuri, Pavitra Patel
 * 
 */
public class UserDataController implements Serializable
{
	/**
	 * A unique identifier for the serialized version of this class.
	 */
	private static final long serialVersionUID = 1L;
	private static UserDataController instance = null;
	private ArrayList<User> users;
	
	/**
	 * The singleton instance of the UserDataController class.
	 */
	public static User currentSessionUser;
	
	/**
	 * Initializes a new instance of the UserDataController class.
	 * This constructor creates a default user 'stock' and initializes it with a default album containing stock photos.
	 */
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
    	
    	Photo p1 = new Photo("Temple1", "data/stockimage1.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	Photo p2 = new Photo("Temple2", "data/stockimage2.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	Photo p3 = new Photo("Temple3", "data/stockimage3.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	Photo p4 = new Photo("Temple4", "data/stockimage4.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	Photo p5 = new Photo("Temple5", "data/stockimage5.jpeg", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")));
    	
    	stockAlbum.addPhotos(p1);
    	stockAlbum.addPhotos(p2);
    	stockAlbum.addPhotos(p3);
    	stockAlbum.addPhotos(p4);
    	stockAlbum.addPhotos(p5);
	}
	
	/**
	 * Returns the singleton instance of the UserDataController class.
	 * If the instance does not exist, it creates a new one.
	 *
	 * @return the singleton instance of the UserDataController class.
	 */
	public static UserDataController getInstance()
	{
		if(instance == null)
		{
			instance = new UserDataController();
		}
		return instance;
	}
	
	/**
	 * Adds a new user to the list of users.
	 *
	 * @param u the user to be added.
	 */
	public void addUser(User u)
	{
		users.add(u);
	}
	
	/**
	 * Deletes a user from the list of users based on the provided username.
	 *
	 * @param username the username of the user to be deleted.
	 */
	public void deleteUser(String username)
	{
		for(User user : users)
		{
			if(user.getUserName().equals(username)) {
				users.remove(user);
				break;
			}
		}
	}
	
	/**
	 * Returns a list of usernames of all the users in the system.
	 *
	 * @return a list of usernames of all the users in the system.
	 */
	public ArrayList<String> getUsernames()
	{
		ArrayList<String> usernames = new ArrayList<String>();
		for(User user: users)
		{
			usernames.add(user.getUserName());
		}
		return usernames;
	}
	
	/**
	 * Checks if the list of users contains a user with the provided username.
	 *
	 * @param u the username to be checked.
	 * @return true if the user exists, false otherwise.
	 */
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
	
	/**
	 * Reads user data from a file and returns the corresponding UserDataController instance.
	 *
	 * @return the UserDataController instance containing the user data read from a file.
	 */
	public static UserDataController readFromAFile()
	{
		UserDataController loadUsers = UserDataController.getInstance();
		File file = new File("data/users.ser");
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
	
	/**
	 * Writes the current user data to a file.
	 */
	public void writeToAFile()
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/users.ser")))
		{
            oos.writeObject(users);
        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
	}
	
	/**
	 * Sets the current session user based on the provided username.
	 *
	 * @param user the username of the user to be set as the current session user.
	 */
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
	
	/**
	 * Returns the current session user.
	 *
	 * @return the current session user.
	 */
	public static User getCurrentSessionUser()
	{
		return currentSessionUser;
	}
}