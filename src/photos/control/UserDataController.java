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
import java.util.ArrayList;

public class UserDataController implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static UserDataController instance = null;
	private ArrayList<User> users;
	
	private UserDataController()
	{
		users = new ArrayList<>();
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
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
		{
			if(file.length() == 0)
			{
				//
			}
			else
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
		catch(Exception e)
		{
	        //
	    }
		
		return loadUsers;
	}
	
	//still need to work on it
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
}